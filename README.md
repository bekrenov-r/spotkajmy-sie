# Spotkajmy się

Algorytm generujący zakresy czasowe znajduje się w klasie <b>TimeRangeService</b>.

Po uruchomieniu program działa pod adresem http://localhost:8080. <br> Aplikacja udostępnia endpoint
<b>POST /generate-time-ranges</b>, który jako request body przyjmuje listę kalendarzy (w postaci JSON array), i jako parametr żądania 'meeting_duration' - oczekiwaną 
długość spotkania (w formacie HH:mm). <br>
Endpoint zwraca listę zakresów w których można zorganiować spotkania. 

Program zawiera walidację ilości kalendarzy (nie mniej dwóch), a także zakresów czasowych podanych w kalendarzach ("start" nie może być później niż "end").
