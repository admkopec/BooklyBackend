package pw.react.tuesday_booklybackend.utils;

public enum CompanionService {
    Carly,
    Flatly,
    Parkly;

    public static CompanionService valueFrom(String string) {
        switch (string) {
            case "parkly":
                return CompanionService.Parkly;
            case "flatly":
                return CompanionService.Flatly;
            case "carly":
                return CompanionService.Carly;
            default:
                return null;
        }
    }
}
