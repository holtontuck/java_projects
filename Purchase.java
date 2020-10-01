package budget;

class Purchase {
    private String description;
    private String type;
    private Float price;

    Purchase(String description, String type, Float price) {
        this.description = description;
        this.type = type;
        this.price = (float)Math.round(price * 100.0)/ 100;
    }

    void setDescription(String description) {
        this.description = description;
    }

    String getDescription() {
        return description;
    }

    void setType(String type) {
        this.type = type;
    }

    String getType() {
        return type;
    }

    void setPrice(Float price) {
        this.price = price;
    }

    Float getPrice() {
        return price;
    }
}

class Food extends Purchase {
    private String description;
    private String type;
    private Float price;

    Food (String description, String type, Float price) {
        super(description, type, price);
    }
}

class Clothes extends Purchase {
    private String description;
    private String type;
    private Float price;

    Clothes (String description, String type, Float price) {
        super(description, type, price);
    }
}

class Entertainment extends Purchase {
    private String description;
    private String type;
    private Float price;

    Entertainment (String description, String type, Float price) {
        super(description, type, price);
    }
}

class Other extends Purchase {
    private String description;
    private String type;
    private Float price;

    Other (String description, String type, Float price) {
        super(description, type, price);
    }
}





