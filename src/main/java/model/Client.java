package model;
/**
 * The Client class represents a client in the system.
 * It holds information about the client such as their id, name, email, phone number, and address.
 * This class is used to create and manipulate Client objects.
 */
public class Client {

    private int client_id;
    private String name;
    private String email;
    private String phone_number;
    private String address;

    public Client () {
    }
    public Client(int client_id, String name, String email, String phone_number, String address) {
        super();
        this.client_id = client_id;
        this.name = name;
        this.email = email;
        this.phone_number = phone_number;
        this.address = address;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    @Override
    public String toString() {
        return this.name;
    }
}
