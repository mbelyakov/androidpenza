# Android Study. https://vk.com/androidpnz

## ДЗ №10

На основе ДЗ №9 сделать:

1) Модель данных пользователя (отдельный класс, типо User)
2) Экран настроек, где будет можно выбрать:
    - размер шрифта текста в карточках
    - порядок карточек (по дате добавления, по алфавиту от А до Я, по алфавиту от Я до А (Сортировка происходит по фамилии)), для сортировки использовать свой компаратор (см. Java Comparable и Comparator)
    - скорость скроллинга карточек
Всю информацию из настроек хранить в SharedPreferences
3) Добавить возможность менять цвет заднего фона карточки, цвет должен выбираться в палитре (Использовать любой color picker, который сможете найти на просторах интернета)
Цвет также должен храниться в базе данных

**Обычное задание**: реализовать хранение и получение информации карточек в SQLite.

**Усложненное задание**: реализовать на экране настроек выбор, где хранить/получать информацию в SQLite или Realm.
