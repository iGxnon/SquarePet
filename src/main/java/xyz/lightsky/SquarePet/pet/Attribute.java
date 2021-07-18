package xyz.lightsky.SquarePet.pet;

public enum  Attribute {

    LAND{
        @Override
        public String toString() {
            return "陆";
        }
    },
    FLY{
        @Override
        public String toString() {
            return "空";
        }
    },
    SWIM{
        @Override
        public String toString() {
            return "水";
        }
    },
    ALL{
        @Override
        public String toString() {
            return "全";
        }
    };

    public static Attribute of(String value) {
        switch (value) {
            case "陆":
                return Attribute.LAND;
            case "空":
                return Attribute.FLY;
            case "水":
                return Attribute.SWIM;
            default:
                return null;
        }
    }
}
