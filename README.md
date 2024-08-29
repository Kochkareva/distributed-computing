
# Лабораторная работа 7. 

### Задание

**Задачи**:

Написать небольшое эссе. А помогут Вам в этом вопросы из списка:

1. Какие алгоритмы и методы используются для балансировки нагрузки?
1. Какие открытые технологии существуют для балансировки нагрузки?
1. Как осуществляется балансировка нагрузки на базах данных?
1. Реверс-прокси как один из элементов балансировки нагрузки.

### Эссе

Балансировка нагрузки в распределенных системах является очень важным элементом проектирования, так как она отвечает за множество таких немаловажных факторов, как снижение затрат, масштабируемости, гибкости и т.д. Однако для каждой системы требуется индивидуальный подход при выборе алгоритмов и методов балансировки нагрузки. Существуют следующие методы: 
- Раунд-робин - в данном методе задачи распределяются по очередям между доступными серверами таким образом, чтобы каждый сервер получил одинаковую нагрузку.
- Взвешенная раунд-робин - данный метод является аналогичным и отличается тем, что каждый сервер имеет вес, указывающий на его способность к обработке задач. Таким образом, чем выше вес, тем больше задач получает сервер.
- Алгоритм наименьшей нагрузки - в данном алгоритме задачи направляются к серверу, который имеет наименьшее количество активных соединий или задач. Таким образом, обеспечивается равномерное распределение нагрузки между серверами.
- Алгоритм наименьшей задержки - в данном алгоритме запросы отправляются на сервер с наименьшей ожидаемой задержкой, которая основана на измерениях времени отклика серверов или их пропускной способности.
- Алгоритм случайного выбора - в данном алгоритме используется случайное распределение задач по доступным серверам. Быстрый, но может оказаться не эффективным и теряется контроль распределения.
- Интеллектуальные алгоритмы - в данном алгоритме изучаются все факторы о сервере и с помощью машинного обучения происходит выбор сервера.

Также применяется ряд технологий для балансировки нагрузки, например:
- Nginx - данный веб-сервер поддерживает различные алгоритмы балансировки нагрузки, например, round-robin. Также обладает возможностью горизонтального масштабирования.
- Apache - представляет собой  прокси-сервер с встроенной поддержкой балансировки нагрузки. Он позволяет настраивать различные алгоритмы балансировки нагрузки, такие как round-robin и least busy. 

Балансировка нагрузки на базах данных также является необходимым, т.к. помогает увеличить производительность, обеспечить отказоустойчисвость и т.д. Для осуществления данной задачи используют различные методы, например:
- репликация данных;
- шардинг, т.е. разбиение базы данных на несколько фрагментов, которые хранятся на отдельной базе данных или сервере;
- балансировка нагрузки на уровне приложения и т.д.

Реверс-прокси как один из элементов балансировки нагрузки имеет важное значение для обеспечения высокой доступности и производительности системы, выполяя такие функции, как: распределение нагрузки, мониторинг состояния серверов баз данных, кэширование данных, обеспечение шифрования и безопасности соедения между клиентами и серверами баз данных.