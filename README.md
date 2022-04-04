# projekt-teleturniej-sieci
Projekt teleturniej na sieci komputerowe
Zasady:

· Gra dla 2-4 osób,

· Każda gra zawiera zbiór 10 pytań losowanych z 20 pytań, na które należy odpowiedzieć TAK lub NIE,

· Pierwszy podłączony gracz zarządza grą (tzn. może ją wystartować),

· W każdej z rund serwer rozsyła zapytania do graczy i czeka na tego, który da poprawną odpowiedź najszybciej,

· Jeżeli w przeciągu 5 sekund nikt nie poda odpowiedzi to serwer przechodzi do następnej rundy,

· Każda poprawna odpowiedź to 1 pkt

· Po 3 błędnych odpowiedziach z rzędu gracz pauzuje 1 rundę.

Podstawowe informacje o projekcie:

· Używa komunikacji TCP,

· Projekt zawiera 2 osobne programy (serwer / klient),

· Komunikacja implementuje odporność na błędy. W razie problemów ze zrozumieniem zapytania od klientów serwer zwaraca odpowiedź ERROR

Dodatkowe elementy:

· Jeżeli gra się rozpoczęła Serwer uniemożliwia dołączenie nowego gracza.

· Istnieje zabezpieczenie przed oszustwem jakiegoś gracza, np. wysłanie odpowiedzi przed zadaniem pytania.
