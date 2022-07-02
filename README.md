# EasyDish
Recipe app where you can search recipes by ingredients you already have at home.

login screen:

![WhatsApp Image 2022-07-02 at 5 25 47 PM](https://user-images.githubusercontent.com/62388952/177004968-93da6227-89c5-4414-a6a2-6882b3b81a39.jpeg)

*** Connecting with google doesnt work at the moment ***

This is the main screen where you can search ingridients by name or by ingredients:

![main](https://user-images.githubusercontent.com/62388952/177005176-e5fd9bbd-4bd9-4655-9140-87c53031933b.jpg)

When searching by ingredients it will autocomplete from a list of ingredients that are in the firebase database.
You can enter as many ingredients as you like and it will show all recipes in the database which cotain those ingredients.

![search_ing](https://user-images.githubusercontent.com/62388952/177005284-d240119a-cd2d-4086-8601-20267443b89c.jpeg)

![search_results](https://user-images.githubusercontent.com/62388952/177005299-6eef99a8-e377-4f6c-bcf4-9ddb34bce095.jpeg)

Viewing a recipe:

![recipe](https://user-images.githubusercontent.com/62388952/177005320-095e553b-b757-41ab-b8ad-532c3d22f121.jpeg)

You can press 'like' on the recipe and it will appear on the favorites page:

![favorites](https://user-images.githubusercontent.com/62388952/177005356-730a6706-ec43-4d4e-9f6b-ff64c276dd38.jpeg)

You can also add your own recipes and view them in the 'My Recipes' page:

![my_recipes](https://user-images.githubusercontent.com/62388952/177005418-e80adc24-f6fd-492e-96d6-9e723cc906fa.jpeg)

![add_recipe](https://user-images.githubusercontent.com/62388952/177005428-54bccf48-9403-44b5-9671-72e7634e29a3.jpeg)

After adding a recipe it wiil upload its details to firebase realtime database and upload the image to firebase storage.

In the action bar menu you can navigate between three options: Favorites, My Recipes and SignOut which will take you back to the login page.

![menu](https://user-images.githubusercontent.com/62388952/177005499-32d3e98d-3206-4f98-8510-0da562f45f11.jpeg)

