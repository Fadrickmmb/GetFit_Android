    package com.example.getfit_android;

    public class Model {

        String email, name, password;
        Integer calorieGoal;


        public Model(String e, String n, String p, int c){
            email = e;
            name = n;
            password = p;
            calorieGoal = c;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }


        public Integer getCalorieGoal() {
            return calorieGoal;
        }

        public void setCalorieGoal(Integer calorieGoal) {
            this.calorieGoal = calorieGoal;
        }
    }
