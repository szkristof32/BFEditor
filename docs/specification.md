% Brainfuck Editor – specifikáció

Szepesfalvy Kristóf, BGP8UG, 2026

# Az alkalmazás célja

Az alkalmazás célja, hogy egy integrált fejlesztői környezetet biztosítson a Brainfuck nevű ezoterikus programozási nyelvhez. Része egy szerkesztő, amelyben a felhasználó a programkódot szerkeszteni tudja, és egy futtató környezet, amelynek segítségével a kész programot ki lehet próbálni

# Az alkalmazás felépítése

Az alkamazás három fő részből áll. Az ablak felső részén található a menüsor, ahol a program legtöbb funkciója elérhető a menürendszeren keresztül. Alatta található a szerkesztő terület, itt találhatóak a megnyitott fájlok. Végül az ablak alsó részén helyezkedik el a futtatókörnyezet, ezen belül található egy virtuális konzol, amely a futásban lévő program be- és kimeneteként szolgál.

## Menüsor

A menüsorban három menü található: file, edit és run.

### File menü

A file menüben található menüpontok segítségével a felhasználó meg tud nyitni, létre tud hozni és el tud menteni fájlokat. A következő menüpontok találhatóak benne:

- New: a menüpontra kattintva létrejön egy új fájl a szerkesztőben. A művelet nem hoz létre új fájlt a lemezen.
- Open: a menüpontra kattintva lehetőség van megnyitni egy már létező fájlt a lemezről. Megnyílik egy felugró ablak, ahol a megfelelő fájlt kiválasztva, és a megnyitás gombra kattintva a szerkesztő beolvassa a fájl tartalmát, és egy új fülön megjeleníti.
- Save: a menüpontra kattintva lehetőség van a jelenleg szerkesztés alatt álló fájl mentésére. Amennyiben még nem létezik hozzá állomány, egy felugró ablak jelenik meg, ahol a felhasználó ki tudja választani a mentési helyet. A mentés gombra kattintva a program a lemezre írja a fájl tartalmát. Ha létezik hozzá tartozó állomány, a felugró ablak nem jelenik meg, hanem a fájl tartalma egyből a lemezre íródik.
- Save as: hasonló a save menüponthoz, de mindig megjelenik felugró ablak, ahol a felhasználó kiválaszthatja, hogy a fájl hova legyen mentve.
- Exit: a menüpontra kattintva bezáródik az alkalmazás. Amennyiben vannak el nem mentett fájlok, a program megjelenít egy felugró ablakot, ahol a felhasználó kiválaszthatja, hogy szeretné-e menteni a fájljait. A Save opciót választva mindegyik nem mentett fájlhoz meghívódik a Save menüponthoz tartozó függvény. A Discard opciót választva a program eldobja a módosításokat. A Cancel opciót választva a program megszakítja a műveletet.

Mind az Open, a Save és a Save as művelet megszakítható, amennyiben a felhasználó a felugró ablakban a Cancel gombra kattint.

### Edit menü

Az edit menü segítségével az aktív fájlon lehet szerkesztési műveleteket végezni. A következő menüpontokat tartalmazza:

- Undo: visszavonja a legutóbbi módosítást. Amennyiben ilyen nincs, a művelet megszakad (lásd: [módosítási előzmények](#módosítási-előzmények)).
- Redo: újra végrehajtja a legutóbb visszavont módosítást. Amennyiben ilyen nincs, a művelet megszakad.
- Cut: a vágólapra másolja és kitörli a bufferből a kijelölt szövegrészletet. Amennyiben semmi sincs kijelölve, a művelet megszakad.
- Copy: a vágólapra másolja a bufferben kijelölt szövegrészletet. Amennyiben semmi sincs kijelölve, a művelet megszakad.
- Paste: beilleszti a bufferbe a vágólapon található legfrissebb szövegrészletet. Amennyiben a vágólap üres, a művelet megszakad.
- Preferences: megnyitja a beállítások ablakot (lásd: [beállítások ablak](#beállítások-ablak)).

### Run menü

A run menü segítségével lehet vezérelni a futtatókörnyezetet. A következő menüpontokat tartalmazza:

- Execute: lefuttatja az aktív fájlban található utasításokat. Mivel a Brainfuckban a nyolc vezérlőkarakter kivételével minden karakter megjegyzésnek minősül, a program minden fájlt értelmes Brainfuck forrásfájlként kezel.
- Debug: elindítja a futtatókörnyezetben az aktív fájl végrehajtását, lehetőséget kínálva a felhasználónak, hogy utasításonként lépkedjen végig a programon.
- Step: végrehajta az aktív fájl soron következő utasítását. Amennyiben nem fut egy program sem, le van tiltva.

## Szerkesztőterület

A szerkesztőterület több fülből áll. Mindegyik fül egy megnyitott fájlnak felel meg. Mindegyik fülön található egy szövegmező, amelyben a fájl (buffer) jelenlegi állapota látható (és szerkeszthető). Az itt látható kódot futtatja a futtatókörnyezet, és ezt menti a program a File > Save vagy File > Save as menüpontra kattintva. Mindegyik fülön szerepel az aktuálisan szerkesztett fájl neve. Mindegyik fül bezárható a fülön található bezárás gombra kattitva. Mindegyik fülön a fájlnév mellett látható csillag karakter (*) jelzi, ha az adott bufferben van nem mentett módosítás.

### Módosítási előzmények

A szerkesztőfelületnek fontos része, hogy az egyes módosítások visszavonhatók és újra végrehajthatók. Ennek érdekében minden bufferhez tartozik egy lista, amely az adott bufferen végzett módosításokat tárolja. Az Edit > Undo és Edit > Redo műveletekkel ebben lehet navigálni.

## Futtatókörnyezet

A futtatókörnyezet maga is két részre oszlik. A tetején egy gombsor helyezkedik el, alatta pedig a virtuális konzol és debug módban a runtime state is.

### A gombsor

A futtatókörnyezet gombsora rendre a menüsor Run menüjének opcióit képzi le (lásd: [Run menü](#run-menü)).

### Virtuális konzol

A virtuális konzol szolgál be- és kimenetként a program számára. A kiírás utasítás hatására az aktív memóriacellában található karakter itt jelenik meg (lásd: [a Brainfuck nyelv](#a-brainfuck-nyelv)), és a beolvasás utasítás hatására innen lesz betöltve az aktív memóriacellába a felhasználó általl beütött karakter.

### Runtime state

Debug módban a virtuális konzol mellett található, itt lehet olvasni a memóriacellák aktuális tartalmát, illetve az instruction pointer értékét (lásd: [a Brainfuck nyelv](#a-brainfuck-nyelv)).

## Beállítások ablak

Az Edit > Preferences menüpontra kattintva jelenik meg felugró ablakként. A program alapvető konfigurálására szolgál. A következő beállítások találhatóak benne:

- Memóriaméret: futtatáskor a programhoz allokált memóriacellák száma (lásd: [a Brainfuck nyelv](#a-brainfuck-nyelv)).
- Memóriacella túlfolyás: mi történjen, ha egy memóriacella tartalma túlfolyik (lásd: [a Brainfuck nyelv](#a-brainfuck-nyelv)).
- Kezdeti cellaérték: a memóriacellák tartalma a program indításakor (lásd: [a Brainfuck nyelv](#a-brainfuck-nyelv)).

Az ablak alján két gomb található. A Save gombra kattintva a program elmenti a beállítások módodítását, és automatikusan szerializálja a lemezre. A Cancel gombra kattintva a program elveti a beállítások módosításait. Mindkét művelet végén a beállítások ablak bezáródik.

# A Brainfuck nyelv

A Brainfuck egy ezoterikus nyelv, első verzióját Urban Müller készítette 1993-ban. Minden brainfuck program indításkor kap egy memóraszegmenst, amely memóriacellákból áll (ezek számát a beállítások ablakban konfigurálni, alapértelmezés: 1024), minden memóriacella egy előjel nélküli nyolc bites egész számot (C-ben: ```uint8_t```) tud tárolni. Minden programhoz tartozik ezen kívül egy instruction pointer is, amely az értelmező pozícióját tárolja a forráskódon belül, és egy data pointer, amely mindig a memóriaszegmens egyik cellájára mutat.

## Utasítások

A Brainfuckban nyolc utasítás található, mindegyik egy karakterből áll, ezek a következőek:

- ```>``` Eggyel megnöveli a data pointert.
- ```<``` Eggyel csökkenti a data pointert. Undefined behaviour, ha ```data pointer == 0```.
- ```+``` Eggyel megnöveli a data pointer által hivatkozott memóriacella értékét modulo 256.
- ```-``` Eggyel csökkenti a data pointer által hivatkozott memóriacella értékét modulo 256.
- ```.``` Kiírja a kimenetre a data pointer által hivatkozott memóriacella értékét.
- ```,``` Beolvas a bemenetről egy értéket, melyet a data pointer által hivatkozott memóriacellába ír.
- ```[``` Ha a data pointer által hivatkozott memóriacella értéke nulla, megnöveli az instruction pointert, hogy a hozzá tartozó ```]``` utasítás utánra mutasson.
- ```]``` Ha a data pointer által hivatkozott memóriacella értéke nemnulla, csökkenti az instruction pointert, hogy a hozzá tartozó ```[``` utasításra mutasson.

## Megjegyzések

A ```[``` és ```]``` utasítások speciális eseteit leszámítva az értelmező minden utasítás után eggyel növeli az instruction pointer értékét, hogy az a következő utasításra mutasson.

A ```[``` és ```]``` utasításokkal tetszőleges mennyiségű ciklus egymásba ágyazható, úgy, hogy a ```[``` és  ```]``` utasítások száma meg kell, hogy egyezzen.

# Megvalósítás

A programkódban külön modulban van a szerkesztő és az értelmező kódja. Az egészet a ```BFEditor``` nevű singleton osztály fogja össze, ezen keresztül tudnak a program különböző részei egymással kommunikálni, és egymás adatait lekérni.

## Értelmező

A Brainfuck nyelv egyszerűsége miatt az értelmezőt meg lehet valósítani egy egyszerű állapotgép segítségével: az aktuális állapotot az instruction pointer által hivatkozott utasítás adja (lásd: [a Brainfuck nyelv](#a-brainfuck-nyelv)).

## Szerkesztő

A szerkesztő egyes részeinek (lásd: [felépítés](#az-alkalmazás-felépítése)) kódja külön osztályokban található, és szorosan össze van kapcsolva a UI felépítésével és a megfelelő Swing osztályokkal.

Például a szerkesztőterület kódja egy ```EditorPanel``` nevű osztályban található, amely a ```JPanel``` leszármazottja, így nagyon egyszerű kezelni, és hozzáadni a programablakhoz.

A menük szintén külön osztályokra vannak bontva a könnyebb kezelhetőség érdekében.
