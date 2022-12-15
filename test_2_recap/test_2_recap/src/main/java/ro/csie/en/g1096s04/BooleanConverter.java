package ro.csie.en.g1096s04;

import androidx.room.TypeConverter;

public class BooleanConverter {

    @TypeConverter
    public static boolean toBoolean(int value) {
        return value == 0 ? false : true;
    }

    @TypeConverter
    public static int toTimestamp(boolean value) {
        return value == false ? 0 : 1;
    }
}
