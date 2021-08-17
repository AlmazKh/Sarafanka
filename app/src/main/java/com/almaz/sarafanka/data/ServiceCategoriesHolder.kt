package com.almaz.sarafanka.data

import com.almaz.sarafanka.core.model.ServiceCategory

class ServiceCategoriesHolder {
    private val categories: MutableList<ServiceCategory> = mutableListOf()

    init {
        categories.add(
            ServiceCategory(
                1,
                "Здоровье врачи",
                listOf(
                    "Телохранитель",
                    "Психолог",
                    "Массаж",
                    "Терапевт",
                    "Детский врач",
                    "Ветеринар",
                    "Стоматолог",
                    "Гинеколог",
                    "Узи",
                    "Хирург",
                    "Дерматолог",
                    "Невролог"
                )
            )
        )
        categories.add(
            ServiceCategory(
                2, "Закон-сделки-финансы", listOf(
                    "Бухгалтер и финансист", "Консультант", "Юрист", "Риелтор", "Нотариус"
                )
            )
        )
        categories.add(
            ServiceCategory(
                3, "Быт и домохозяйство", listOf(
                    "Уборка",
                    "Сантехник",
                    "Электрик",
                    "Садовник",
                    "Дизайнер интерьера",
                    "Сборщик мебели и кухонь",
                    "Ремонт одежды",
                    "Химчистка",
                    "Повар"
                )
            )
        )
        categories.add(
            ServiceCategory(
                4,
                "Стройка и ремонт",
                listOf(
                    "Фундамент",
                    "Архитектор",
                    "Сварщик",
                    "Каркасные дома",
                    "Покраска",
                    "Обои",
                    "Окна",
                    "Двери",
                    "Камещики",
                    "Заборы",
                    "Электрики",
                    "Штукатур",
                    "Разнорабочий",
                    "Мебель",
                    "Отделка",
                    "Строительство домов и коттеджей",
                    "Ремонт квартир",
                    "Сантехника и отопление",
                    "Электромонтаж"
                )
            )
        )
        categories.add(
            ServiceCategory(
                5,
                "Красота и спорт",
                listOf(
                    "Парикмахер",
                    "Маникюр-педикюр макияж",
                    "Ресницы и брови",
                    "Косметология эпиляция",
                    "Спа и массаж",
                    "Фитнес",
                    "Питание",
                    "Йога",
                    "Боевые искусства",
                    "Единоборства",
                    "Шугаринг",
                    "Тренер",
                    "Тату-пирсинг",
                    "Стилист"
                )
            )
        )
        categories.add(
            ServiceCategory(
                6, "Авторемонт", listOf(
                    "Автомеханик",
                    "Шиномонтаж",
                    "Автомойщик",
                    "Автоэлектрик",
                    "Автопокраска",
                    "Техосмотр"
                )
            )
        )
        categories.add(
            ServiceCategory(
                7,
                "Дети и обучение",
                listOf(
                    "Няня",
                    "Сиделка",
                    "Логопед",
                    "Танцы",
                    "Рисование",
                    "Руковделие",
                    "Иностранные языки",
                    "Репетитор",
                    "Музыка",
                    "Вождение"
                )
            )
        )
        categories.add(
            ServiceCategory(
                8, "Бизнес и реклама", listOf(
                    "Охрана", "Дизайн", "СММ", "Маркетинг", "Реклама в Интернете", "Полиграфия",
                    "Фотосьемка", "Видеосьемка"
                )
            )
        )
        categories.add(
            ServiceCategory(
                9, "ПК, IT, Техника", listOf(
                    "Сайт",
                    "ПК",
                    "Антивирус",
                    "Установка ОС",
                    "Настройка интернета",
                    "Установка домашней техники",
                    "Ремонт телефонов",
                    "Ремонт техники"
                )
            )
        )
        categories.add(
            ServiceCategory(
                10,
                "Курьеры и грузы",
                listOf(
                    "Доставка",
                    "Грузчики",
                    "Перезды",
                    "Грузоперевозка",
                    "Спецтехника",
                    "Вывоз мусора",
                    "Перевозка стройматериала",
                    "Эвакуатор"
                )
            )
        )
        categories.add(
            ServiceCategory(
                11, "Артисты", listOf(
                    "Аниматор", "Ведущий", "Певец", "Танцор", "Актер"
                )
            )
        )
    }

    fun getCategories(): List<ServiceCategory> = categories

    fun getCategoryById(id: Int) = getCategories().find { category -> category.id == id}

    fun getCategoryIdByName(name: String): Int = categories.find {category -> category.name == name }?.id ?: 1
}
