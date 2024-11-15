# **Overview**

**GetFit** is a comprehensive health and fitness app designed to help users track their daily meals, monitor caloric intake, and manage macronutrients (protein, fats, and carbohydrates). With Firebase Realtime Database integration, GetFit ensures seamless data storage and retrieval, enabling users to maintain their dietary goals efficiently.

### **Features**

1. Daily Meal Logging
   - Add meals to specific days.
   - View a list of meals for the current day with names and calorie counts.

2. Nutritional Information Integration
   - Automatically fetch meal calorie and macronutrient data using the CalorieNinjas API.
   - Display detailed nutritional breakdown for each meal.
  
3. User Management
   - Firebase Authentication for secure user login and registration.
   - Each user has a personalized data structure in Firebase, ensuring privacy and data separation.
  
4. Day Management
   - Start each user with a default "Day 1" entry upon registration.
   - Maintain a list of days where each day includes its meals and total nutritional values.

5. Firebase Realtime Database Integration
   - Store user profiles, days, and meals efficiently.
   - Link users to their meal and day data via a structured database schema.
  


### **Technical Details**


#### **Architecture**
- **Frontend**: Android (XML layouts and Java/Kotlin)
- **Backend**: Firebase Realtime Database for storing user data and CalorieNinjas API for fetching nutritional information.

#### **Database Structure**
1. Users Table:
   - Key: User's Email
   - Fields:
      * email: User's email
      * UUID: User's Unique Identifier
      * Other user details
      * 
2. Days Table:
   - Key: User's Email
   - Fields:
      * dayId: Unique identifier for the day
      * meals: List of meals of the day
      * dayCalories, dayProtein, dayFat, dayCarbs: Summary of daily nutrition

3. Meals Table:
   - Fields:
      * name: Meal Name
      * description: Short description of the meal
      * details: Detailed meal information
      * Calories, Proteins, Fat, Carbs: Nutritional Values
    
#### **API Integration**
- **CalorieNinjas API**:
   * Used to fetch calorie and macronutrient data for user-entered meals.


### **How To Use**

1. **User Registration and Login**
   - Create an account or log in using your email and password.

2. **Logging Meals**
   - Tap on "Add Meal" to input meal details.
   - Fetch nutritional information automatically or input it manually.

3. **Viewing Day's Meals**
   - Navigate to the "Today" screen to view your meals and their nutritional values for the current day.
  
4. **Managing Days**
   - View the history of your logged days and their nutritional summaries.
  

### **Future Enhancements**

- Add a graphical representation of daily and weekly nutritional progress.
- Implement reminders for meal logging and hydration tracking.
- Support for multiple languages.


### **License**

This project is licensed under the [MIT License](https://choosealicense.com/licenses/mit/)


### **Contact**

For any queries or suggestions, please contact:
- **Developer**: Fadrick Barroso
- **Email**: [fadrick.barroso@gmail.com]
- **LinkedIn**: [https://www.linkedin.com/in/fadrick-barroso/] 

