# Задача:
Написать приложение, отображающее вывод товаров, используя данные из https://dummyjson.com/products.
Приложение должно подгружать данные из интернета, кэширование не требуется.
Обязательно реализовать отображение полей title, description и thumbnail. Дизайн можно выбрать на свое усмотрение, но реализация должна соответствовать material guidelines. 
Данные необходимо загружать страницами по 20 штук (управляется query параметрами skip и limit. Например, /products? skip=40&limit=20 выведет 20 продуктов, начиная с 41-го). 
Использование сторонних библиотек не воспрещается, но оно должно быть обоснованным.

### Рекомендации:
Для написания использовать kotlin (предпочтительнее) или java, для верстки можно использовать Android views (xml) или compose, 
для асинхронного выполнения можно использовать rxjava (на проекте более распространена именно она) или coroutines, для загрузки картинок — Glide,
Fresco или любую другую библиотеку на свое усмотрение.

### Дополнительно:
Можно поддержать функционал, например:
1. Отображениедополнительныхполей (например, цен или остальных изображений товара).
2. Переходнаэкрантовара.
3. Поиск(каклокальный,такичерезбэкенд, с помощью запроса /products/search?q= <запрос>).
4. Сортировкапокатегориям(информацию о категориях можно получить через запрос /products/categories, список товаров в категории через /products/category/<категория>).
Обращаем внимание, что это дополнительный функционал. В первую очередь будет оцениваться проработка обязательной части. Например, приложение с дополнительным функционалом, но вылетающее при получении сетевой ошибки на запрос /products будет оценено ниже,
чем приложение без дополнительного функционала, но с корректной обработкой ошибок.

# Realisation

### Stack
- Kotlin
- Coroutines
- Flow
- Retrofit
- Compose
- Clean arch
- Dagger2
- MVI

### Screenshots
<img width="297" alt="image" src="https://github.com/swed397/vk-tech-task/assets/28994194/363c89ec-5fb0-48b5-b3f6-e3d028acb2a1">
<img width="299" alt="image" src="https://github.com/swed397/vk-tech-task/assets/28994194/55156a4c-69d9-428f-b943-fb146e0dc4ee">
<img width="303" alt="image" src="https://github.com/swed397/vk-tech-task/assets/28994194/4e4cd200-b4f4-47c6-800c-4e9f5f00328c">
<img width="303" alt="image" src="https://github.com/swed397/vk-tech-task/assets/28994194/ed5d0b79-ab5d-4a64-a01d-0c479ee5b123">
<img width="301" alt="image" src="https://github.com/swed397/vk-tech-task/assets/28994194/182a3dc9-1c1f-45aa-975d-56feb6e58eb5">
<img width="297" alt="image" src="https://github.com/swed397/vk-tech-task/assets/28994194/6fbf12fd-6a55-49d7-b4e4-f714224a4cfe">

### Demonstration
https://github.com/swed397/vk-tech-task/assets/28994194/09aacd32-9692-4d3f-91d8-733aa33a1789
https://github.com/swed397/vk-tech-task/assets/28994194/5ab93356-20e0-435a-8a26-62fb9740d269
https://github.com/swed397/vk-tech-task/assets/28994194/74243a88-ea3a-465f-a532-1d548acfebf9


