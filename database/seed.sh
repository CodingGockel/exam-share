#!/usr/bin/env bash
# Fills a running instance with sample exams and votes via the REST API (so all validation applies).
#
#   ./database/seed.sh                         # against the production stack (http://localhost)
#   BASE_URL=http://localhost:8080 ./database/seed.sh   # directly against `./gradlew :backend:bootRun`
#
# Each vote is sent without cookie, so the backend treats it as a new browser.
set -euo pipefail

BASE_URL="${BASE_URL:-http://localhost}"
PDF_DIR="$(cd "$(dirname "$0")" && pwd)/dummy-pdfs"

command -v curl >/dev/null || { echo "curl fehlt" >&2; exit 1; }
command -v jq >/dev/null || { echo "jq fehlt" >&2; exit 1; }

upload() { # university faculty program degree subject lecturer semester file
    curl -sS --fail-with-body -X POST "$BASE_URL/api/exams" \
        -F "university=$1" -F "faculty=$2" -F "studyProgram=$3" -F "degree=$4" \
        -F "subject=$5" -F "lecturer=$6" -F "semester=$7" \
        -F "file=@$PDF_DIR/$8;type=application/pdf" | jq -r .id
}

vote() { # id direction count
    local i
    for ((i = 0; i < $3; i++)); do
        curl -sS --fail-with-body -o /dev/null -X POST "$BASE_URL/api/exams/$1/votes" \
            -H 'Content-Type: application/json' -d "{\"direction\":\"$2\"}"
    done
}

seed() { # university faculty program degree subject lecturer semester file upvotes downvotes
    local id
    id="$(upload "$1" "$2" "$3" "$4" "$5" "$6" "$7" "$8")"
    vote "$id" UP "$9"
    vote "$id" DOWN "${10}"
    printf '  %-24s %-24s %-8s +%-3s -%-3s %s\n' "$1" "$5" "$7" "$9" "${10}" "$id"
}

UL="Universität Leipzig"
MI="Fakultät für Mathematik und Informatik"
TUD="TU Dresden"

echo "Seede $BASE_URL ..."
seed "$UL"  "$MI" "Informatik"     BACHELOR     "Analysis I"              "Prof. Dr. Weber"    "WS23/24" analysis-1_ws23-24.pdf              18 2
seed "$UL"  "$MI" "Mathematik"     BACHELOR     "Lineare Algebra"         "Dr. Schulz"         "SS24"    lineare-algebra_ss24.pdf             1  0
seed "$TUD" "Fakultät Informatik" "Informatik" BACHELOR "Programmieren 1"   "Prof. Dr. Hoffmann" "WS24/25" programmieren-1_ws24-25.pdf          0  0
seed "$UL"  "$MI" "Informatik"     MASTER       "Datenbanken"             "Dr. Becker"         "SS25"    datenbanken_ss25.pdf                 1  5
seed "$TUD" "Fakultät Informatik" "Informatik" BACHELOR "Theoretische Informatik" "Prof. Dr. Wagner"   "WS22/23" theoretische-informatik_ws22-23.pdf  4  4
seed "$UL"  "Medizinische Fakultät" "Humanmedizin" STAATSEXAMEN "Anatomie"  "Prof. Dr. Klein"    "SS23"    anatomie_ss23.pdf                    2  3
echo "Fertig."
