package businessLayer;

import model.Client;
import java.util.regex.Pattern;

/**
 * The ClientBLL class provides business logic for operations on Client objects.
 * It includes methods for validating the fields of a Client object.
 */
public class ClientBLL {
    private static final String EMAIL_PATTERN = "^(.+)@(.+)$";
    private static final String PHONE_PATTERN = "^[0-9]{10}$";

    /**
     * Validates the fields of a Client object.
     * It checks if the email and phone number are in the correct format,
     * and if the name and address have an appropriate length.
     * If a field is not valid, it throws an IllegalArgumentException.
     *
     * @param client the Client object to be validated
     * @throws IllegalArgumentException if a field is not valid
     */
    public void validate(Client client) {
        if (!Pattern.compile(EMAIL_PATTERN).matcher(client.getEmail()).matches()) {
            throw new IllegalArgumentException("Invalid email format!");
        }

        if (!Pattern.compile(PHONE_PATTERN).matcher(client.getPhone_number()).matches()) {
            throw new IllegalArgumentException("Invalid phone number format! The phone number should contain exactly 10 digits.");
        }

        if (client.getName().length() < 3) {
            throw new IllegalArgumentException("Name is too short!");
        }

        if (client.getAddress().length() < 5) {
            throw new IllegalArgumentException("Address is too short!");
        }

        if (client.getName().length()>44){
            throw new IllegalArgumentException("Name is too long!");
        }

        if(client.getAddress().length()>44){
            throw new IllegalArgumentException("Address is too long!");
        }

        if(client.getEmail().length()>44){
            throw new IllegalArgumentException("Email is too long!");
        }
    }
}