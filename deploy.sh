#!/usr/bin/env bash
# Production deployment of the Prüfprot stack via Docker Compose.
#
#   ./deploy.sh [up]          build images, start the stack, wait until healthy
#   ./deploy.sh down          stop the stack (volumes are kept)
#   ./deploy.sh logs [svc]    follow container logs (all services or one)
#   ./deploy.sh serverlog     follow the backend log file (server-log in volume backend-logs)
#   ./deploy.sh status        container and health status
#
# HEALTH_TIMEOUT (seconds, default 180) limits how long `up` waits for the healthchecks.
set -euo pipefail

cd "$(dirname "$0")"

COMPOSE_FILE="docker-compose.yml"
HEALTH_TIMEOUT="${HEALTH_TIMEOUT:-180}"

die() {
    echo "Fehler: $*" >&2
    exit 1
}

compose() {
    docker compose -f "$COMPOSE_FILE" "$@"
}

check_prerequisites() {
    command -v docker >/dev/null 2>&1 || die "Docker ist nicht installiert."
    docker compose version >/dev/null 2>&1 || die "Docker Compose (Plugin) ist nicht installiert."
    docker info >/dev/null 2>&1 || die "Der Docker-Daemon läuft nicht oder ist nicht erreichbar."
    [[ -f .env ]] || die ".env fehlt. Vorlage kopieren: cp .env.example .env"
}

proxy_port() {
    local port
    port="$(grep -E '^PROXY_PORT=' .env | tail -n 1 | cut -d= -f2- || true)"
    echo "${port:-80}"
}

cmd_up() {
    echo "Baue Images ..."
    compose build
    echo "Starte Stack und warte auf Healthchecks (max. ${HEALTH_TIMEOUT}s) ..."
    if ! compose up -d --wait --wait-timeout "$HEALTH_TIMEOUT"; then
        cmd_status
        die "Der Stack ist nicht innerhalb von ${HEALTH_TIMEOUT}s healthy geworden. Logs: ./deploy.sh logs"
    fi
    cmd_status
    echo
    echo "Prüfprot läuft auf http://localhost:$(proxy_port)"
}

cmd_down() {
    compose down
    echo "Stack gestoppt (Volumes bleiben erhalten)."
}

cmd_logs() {
    compose logs -f --tail=200 "$@"
}

cmd_serverlog() {
    compose exec backend tail -n 200 -f /app/logs/server-log
}

cmd_status() {
    compose ps
    echo
    echo "Health-Status:"
    local containers
    containers="$(compose ps -q)"
    if [[ -z "$containers" ]]; then
        echo "  (keine laufenden Container)"
        return 0
    fi
    # shellcheck disable=SC2086
    docker inspect --format '  {{index .Config.Labels "com.docker.compose.service"}}: {{if .State.Health}}{{.State.Health.Status}}{{else}}{{.State.Status}}{{end}}' $containers
}

main() {
    local command="${1:-up}"
    [[ $# -gt 0 ]] && shift

    case "$command" in
        up | down | logs | serverlog | status) ;;
        -h | --help | help)
            sed -n '2,10p' "$0" | sed 's/^# \{0,1\}//'
            exit 0
            ;;
        *) die "Unbekannter Befehl '$command'. Erlaubt: up, down, logs [service], serverlog, status" ;;
    esac

    check_prerequisites
    "cmd_$command" "$@"
}

main "$@"
