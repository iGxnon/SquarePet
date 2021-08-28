package cc.igxnon.square.pet.pets;

/**
 * @author iGxnon
 * @date 2021/08/28
 */
public enum  Attribute {

    Metal{
        @Override
        public String toString() {
            return super.toString();
        }
    },
    Wood{
        @Override
        public String toString() {
            return super.toString();
        }
    },
    Water{
        @Override
        public String toString() {
            return super.toString();
        }
    },
    Fire{
        @Override
        public String toString() {
            return super.toString();
        }
    },
    Earth{
        @Override
        public String toString() {
            return super.toString();
        }
    };

    public static Attribute get(String str) {
        return null;
    }

    public interface Metal {
        default String str() {
            return "";
        }
    }

    public interface Wood {
        default String str() {
            return "";
        }
    }

    public interface Water {
        default String str() {
            return "";
        }
    }

    public interface Fire {
        default String str() {
            return "";
        }
    }

    public interface Earth {
        default String str() {
            return "";
        }
    }

}
