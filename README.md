# System Zarządzania Lekarzami NFZ - Instrukcja Obsługi UI

## Spis treści

1. [Uruchomienie aplikacji](#uruchomienie-aplikacji)
2. [Interfejs użytkownika](#interfejs-użytkownika)
3. [Funkcjonalności](#funkcjonalności)
   - [Przeglądanie listy lekarzy](#1-przeglądanie-listy-lekarzy)
   - [Dodawanie lekarza](#2-dodawanie-lekarza)
   - [Wyświetlanie szczegółów lekarza](#3-wyświetlanie-szczegółów-lekarza)
   - [Usuwanie lekarza](#4-usuwanie-lekarza)
   - [Inicjalizacja bazy danych](#5-inicjalizacja-bazy-danych)
   - [Odświeżanie listy](#6-odświeżanie-listy)

---

## Uruchomienie aplikacji

### Wymagania

- Java 17 lub nowsza
- Gradle (zawarty w projekcie jako wrapper)

### Kroki uruchomienia

1. **Otwórz terminal** w katalogu głównym projektu

2. **Uruchom backend Spring Boot:**

   ```bash
   ./gradlew bootRun
   ```

   Na Windows:

   ```cmd
   gradlew.bat bootRun
   ```

3. **Otwórz przeglądarkę** i przejdź pod adres:

   ```
   http://localhost:8080
   ```

4. Interfejs użytkownika powinien się załadować automatycznie.

---

## Interfejs użytkownika

Po uruchomieniu aplikacji zobaczysz stronę główną z:

- **Nagłówkiem** z nazwą systemu
- **Przyciskami akcji** (Dodaj Lekarza, Odśwież Listę, Inicjalizuj Bazę Danych)
- **Tabelą** z listą lekarzy

---

## Funkcjonalności

### 1. Przeglądanie listy lekarzy

**Opis:** Lista wszystkich lekarzy w systemie wyświetla się automatycznie po załadowaniu strony.

**Jak użyć:**

- Lista lekarzy wyświetla się w tabeli na stronie głównej
- Dla każdego lekarza widoczne są: ID, Imię, Nazwisko, Specjalizacja oraz przyciski akcji

---

### 2. Dodawanie lekarza

**Opis:** Funkcja umożliwia dodanie nowego lekarza do bazy danych.

**Jak użyć:**

1. **Kliknij przycisk** `Dodaj Lekarza` znajdujący się na górze strony

2. **Wyświetli się formularz** z polami do wypełnienia

3. **Wypełnij formularz** przykładowymi danymi:

   - Imię: `Jan`
   - Nazwisko: `Kowalski`
   - PESEL: `90010112345`
   - Specjalizacja: `Pediatria`
   - Ulica: `Słoneczna 15`
   - Miasto: `Warszawa`
   - Kod pocztowy: `00-001`

4. **Kliknij przycisk** `Dodaj Lekarza`

5. **Rezultat:**
   - Formularz zostanie zamknięty
   - Wyświetli się zielony komunikat: "Lekarz został pomyślnie dodany!"
   - Nowy lekarz pojawi się na liście

---

### 3. Wyświetlanie szczegółów lekarza

**Opis:** Funkcja umożliwia wyświetlenie pełnych informacji o wybranym lekarzu, włącznie z danymi adresowymi.

**Jak użyć:**

1. **Znajdź lekarza** na liście, którego szczegóły chcesz zobaczyć

2. **Kliknij przycisk** `Szczegóły` przy wybranym lekarzu

3. **Wyświetli się okno** ze szczegółowymi informacjami

4. **Kliknij** `Zamknij` lub obszar poza oknem, aby zamknąć szczegóły

---

### 4. Usuwanie lekarza

**Opis:** Funkcja umożliwia usunięcie lekarza z bazy danych.

**Jak użyć:**

1. **Znajdź lekarza** na liście, którego chcesz usunąć

2. **Kliknij przycisk** `Usuń` przy wybranym lekarzu

3. **Wyświetli się okno potwierdzenia**

4. **Kliknij** `Usuń` aby potwierdzić usunięcie, lub `Anuluj` aby anulować

5. **Rezultat:**
   - Wyświetli się zielony komunikat: "Lekarz został pomyślnie usunięty!"
   - Lekarz zniknie z listy

---

### 5. Inicjalizacja bazy danych

**Opis:** Funkcja resetuje bazę danych do stanu początkowego z predefiniowaną listą lekarzy. Przydatne do testowania lub przywrócenia danych demonstracyjnych.

**Jak użyć:**

1. **Kliknij przycisk** `Inicjalizuj Bazę Danych` znajdujący się na górze strony

2. **Rezultat:**
   - Wszystkie dotychczasowe dane zostaną usunięte
   - Baza zostanie wypełniona 7 przykładowymi lekarzami:
     - Gregory House (Kardiologia)
     - Allison Cameron (Kardiologia)
     - Robert Chase (Kardiologia)
     - Eric Foreman (Neurologia)
     - Remy Hadley (Neurologia)
     - James Wilson (Onkologia)
     - Lisa Cudy (Urologia)
   - Wyświetli się komunikat: "Baza danych została zainicjalizowana!"
   - Lista lekarzy zostanie odświeżona

**Uwaga:** Ta operacja usunie wszystkie ręcznie dodane dane!

---

### 6. Odświeżanie listy

**Opis:** Funkcja pozwala na ręczne odświeżenie listy lekarzy (przydatne gdy dane mogły zostać zmienione z innego źródła).

**Jak użyć:**

1. **Kliknij przycisk** `Odśwież Listę` znajdujący się na górze strony

2. **Rezultat:** Lista lekarzy zostanie ponownie pobrana z serwera i wyświetlona

---

## Rozwiązywanie problemów

### Problem: "Błąd połączenia z serwerem"

**Rozwiązanie:** Upewnij się, że backend Spring Boot jest uruchomiony (komenda `gradlew bootRun`)

### Problem: Pusta lista lekarzy

**Rozwiązanie:** Kliknij `Inicjalizuj Bazę Danych` aby wypełnić bazę przykładowymi danymi

### Problem: Formularz nie działa

**Rozwiązanie:** Upewnij się, że wszystkie pola formularza są wypełnione - wszystkie pola są wymagane

---

## Informacje techniczne

- **Frontend:** HTML5 + CSS3 + JavaScript (Vanilla)
- **Backend:** Spring Boot 3.x
- **Baza danych:** H2 (in-memory/file)
- **API:** REST
- **Port:** 8080
