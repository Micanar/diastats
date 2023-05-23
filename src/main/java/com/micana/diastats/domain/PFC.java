    package com.micana.diastats.domain;

    import javax.persistence.Entity;
    import javax.persistence.GeneratedValue;
    import javax.persistence.GenerationType;
    import javax.persistence.Id;

    @Entity
    public class PFC {
        @Id
        @GeneratedValue(strategy= GenerationType.AUTO)
        private Integer id;

        private String name;

        private double proteins;
        private double fats;
        private double carbohydrates;
        private double breadUnits;

        public PFC() {
        }

        public double getBreadUnits() {
            return breadUnits;
        }

        public void setBreadUnits(double breadUnits) {
            this.breadUnits = breadUnits;
        }

        public PFC(String name, double proteins, double fats, double carbohydrates, double breadUnits) {
            this.name = name;
            this.proteins = proteins;
            this.fats = fats;
            this.carbohydrates = carbohydrates;
            this.breadUnits=breadUnits;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getProteins() {
            return proteins;
        }

        public void setProteins(double proteins) {
            this.proteins = proteins;
        }

        public double getFats() {
            return fats;
        }

        public void setFats(double fats) {
            this.fats = fats;
        }

        public double getCarbohydrates() {
            return carbohydrates;
        }

        public void setCarbohydrates(double carbohydrates) {
            this.carbohydrates = carbohydrates;
        }
    }
