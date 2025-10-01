import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        //Read input.txt
        List<String> linesFromFile = Files.readAllLines(Paths.get("input.txt"));

        //Ensure that input.txt is not empty
        if (linesFromFile.isEmpty()) {
            System.err.println("Fel: input.txt är tom");
        return; //If empty: print error messeage and exit the program

        }

        //Data Structure
        List<Building> buildings = new ArrayList<>(); //Main list where Building obejcts will be stored,Each Building obejct may be followed by an O, C or A 
       
        //Pointers to the current Building/Owner/Company object operated on
        Building currentBuilding = null; 
        Owner currentOwner = null;
        Company currentCompany = null;

        //Iterate through each line of input.txt
        for (int i = 0; i < linesFromFile.size(); i++) {
            String line = linesFromFile.get(i).trim();
            if(line.isEmpty()) continue;

            //Divide each line into multiple parts
            String[] parts = line.split("\\|");

            //Ensure that each line in input.txt starts with a valid letter [BACOT]
            String type  = parts[0].trim();
            if(!type.matches("[BACOT]")){
                System.err.println("Input-error: Invalid Letter(s) on line: " + (i+1) + ": starts with '" + type + "'");
                return;
            }

            //Insert item into builidngs-list depending on what case it is
            switch (parts[0]) {
                case "B":
                    //Check if line if formatted correctly
                    if(parts.length != 2){
                    System.err.println("Input-error: Building on line " + (i+1) + " mmissing field. Should be B|name");
                    return;
                    }
                    //Check if any field is empty
                    if(parts[1].trim().isEmpty()){
                    System.err.println("Input-error: Building on line " + (i+1) + " missing name");
                    return;
                    }
                    //If all checks is passed- insert Building obejct into list
                    currentBuilding = new Building();
                    currentBuilding.name = parts[1];
                    buildings.add(currentBuilding);
                    currentOwner = null;
                    currentCompany = null;
                    break;

                case "A": //Address case
                    
                    //Check if the Address follows a correct Object [B, O or C]
                    if (currentCompany == null && currentOwner == null && currentBuilding == null) {
                    System.err.println("Input-error: Address on line " + (i+1) + " missing Building, Owner or Company.");
                    return; 
                    }
                    //Check if format is correct
                    if(parts.length != 4){
                        System.err.println("Input-error: Address on line " + (i+1) + " missing field. Should be A|street|city|zipcode");
                        return;
                    }
                    //Check missing fields
                    if(parts[1].trim().isEmpty()){
                    System.err.println("Input-error: Address on line " + (i+1) + " missing street");
                    return;
                    }
                    else if(parts[2].trim().isEmpty()){
                    System.err.println("Input-error: Address on line " + (i+1) + " missing city");
                    return;    
                    }
                    else if(parts[3].trim().isEmpty()){
                    System.err.println("Input-error: Address on line " + (i+1) + " missing zipcode");
                    return;    
                    }

                    //Create new Address object
                    Address addr = new Address();
                    addr.street = parts[1];
                    addr.city = parts[2];
                    addr.zipcode = parts[3];

                    //Check wich object: [B,O,C] we are operating under
                    //Add to list if not a duplicate
                    if (currentOwner != null && !currentOwner.addresses.contains(addr)) { //Check if Owner pointer is null
                        currentOwner.addresses.add(addr);
                    }
                     else if (currentCompany != null && !currentCompany.addresses.contains(addr)) { //Check if Company pointer is null
                        currentCompany.addresses.add(addr);
                    } 
                    else if (currentBuilding != null && !currentBuilding.addresses.contains(addr)) { //Check if Building pointer is null
                        currentBuilding.addresses.add(addr);
                    }
                    break;

                case "O": //Owner case

                    //Check if the Owner follows the correct Object [B]
                    if (currentBuilding == null) {
                    System.err.println("Input-error: Owner on line " + (i+1) + " missing Building.");
                    return;
                    }
                    //Check if format is correct
                    if(parts.length != 2){
                    System.err.println("Input-error: Owner on line" + (i+1) + " missing field. Should be O|name");
                    return;
                    }
                    //Check missing fields
                    if(parts[1].trim().isEmpty()){
                    System.err.println("Input-error: Owner on line" + (i+1) + " missing name");
                    return;
                    }

                    //Create new Owner object and add to list
                    currentOwner = new Owner();
                    currentOwner.name = parts[1];
                    currentBuilding.owners.add(currentOwner);
                    currentCompany = null;
                    break;

                case "C": //Company case

                    //Check if the Company follows the correct Object [B]
                    if (currentBuilding == null) {
                    System.err.println("Input-error: Company on line " + (i+1) + " missing Building.");
                    return;
                    }
                    //Check if format is correct
                    if(parts.length != 3){
                    System.err.println("Input-error: Company on line " + (i+1) + " missing field. Should be C|name|type");
                    return;
                    }
                    //Check missing fields
                    if(parts[1].trim().isEmpty()){
                    System.err.println("Input-error: Company on line" + (i+1) + " missing name");
                    return;
                    }
                    if(parts[2].trim().isEmpty()){
                    System.err.println("Input-error: Company on line " + (i+1) + " missing type");
                    return;
                    }

                    //Create new Company obejct and add to list
                    currentCompany = new Company();
                    currentCompany.name = parts[1];
                    currentCompany.type = parts[2];
                    currentBuilding.companies.add(currentCompany);
                    currentOwner = null;
                    break;

                case "T": //Telephone case

                    //Check if the Company follows the correct Object [O, C]
                    if (currentCompany == null && currentOwner == null) {
                    System.err.println("Input-error: T on line " + (i+1) + " missing Owner or Company.");
                    return;
                    }
                    //Check if format is correct
                    if(parts.length != 3){
                    System.err.println("Input-error: Telephone on line " + (i+1) + " missing field. Should be T|number|faxnumber");
                    return;
                    }
                    //Check missing fields
                    if(parts[1].trim().isEmpty()){
                    System.err.println("Input-error: Telephone on line " + (i+1) + " missing number");
                    return;
                    }
                    if(parts[2].trim().isEmpty()){
                    System.err.println("Input-error: Telephone on line " + (i+1) + " missing faxnumber");
                    return;
                    }
                    
                    //Create a new  Telephone object
                    Phone phone = new Phone();
                    phone.landline = parts[1];
                    phone.fax = parts[2];
                    
                    //Check wich obejct: [O,C] we are operating under
                    //Add to list if not a duplicate (Keeps the most recent phone)
                    if (currentOwner != null) {
                        if (!phone.equals(currentOwner.phone)) {
                            currentOwner.phone = phone;
                        }
                    } else if (currentCompany != null) {
                        if (!phone.equals(currentCompany.phone)) {
                            currentCompany.phone = phone;
                        }
                    }
                    break;
            }
        }

        //Build the XML structure
        //Iterate through the builidngs list and append to xml string
        StringBuilder xml = new StringBuilder(); //Large changeble string - good for blocks
        xml.append("<buildings>\n"); //Root
        for (Building b : buildings) { //Start by iterating through all the buildings
            xml.append("  <building>\n");
            xml.append("    <name>").append(b.name).append("</name>\n");

            for (Address a : b.addresses) { //Append Addresses realting to buildings
                xml.append("    <address>\n");
                xml.append("      <street>").append(a.street).append("</street>\n");
                xml.append("      <city>").append(a.city).append("</city>\n");
                xml.append("      <zipcode>").append(a.zipcode).append("</zipcode>\n");
                xml.append("    </address>\n");//End block
            }

            for (Owner o : b.owners) { //Append Owner related to building
                xml.append("    <owner>\n");
                xml.append("      <name>").append(o.name).append("</name>\n");
                if (o.phone != null) { //If Owner have a Telphone append it
                    xml.append("      <phone>\n");
                    xml.append("        <landline>").append(o.phone.landline).append("</landline>\n");
                    xml.append("        <fax>").append(o.phone.fax).append("</fax>\n");
                    xml.append("      </phone>\n");//End block
                }
                for (Address a : o.addresses) {//If Owner have an Address append it
                    xml.append("      <address>\n");
                    xml.append("        <street>").append(a.street).append("</street>\n");
                    xml.append("        <city>").append(a.city).append("</city>\n");
                    xml.append("        <zipcode>").append(a.zipcode).append("</zipcode>\n");
                    xml.append("      </address>\n");
                }
                xml.append("    </owner>\n");//End block
            }

            for (Company c : b.companies) {//Append Company related to building
                xml.append("    <company>\n");
                xml.append("      <name>").append(c.name).append("</name>\n");
                xml.append("      <type>").append(c.type).append("</type>\n");
                if (c.phone != null) {//If Company have a Telephone append it
                    xml.append("      <phone>\n");
                    xml.append("        <landline>").append(c.phone.landline).append("</landline>\n");
                    xml.append("        <fax>").append(c.phone.fax).append("</fax>\n");
                    xml.append("      </phone>\n");//End block
                }
                for (Address a : c.addresses) {//If Company have an Address append it
                    xml.append("      <address>\n");
                    xml.append("        <street>").append(a.street).append("</street>\n");
                    xml.append("        <city>").append(a.city).append("</city>\n");
                    xml.append("        <zipcode>").append(a.zipcode).append("</zipcode>\n");
                    xml.append("      </address>\n");
                }
                xml.append("    </company>\n"); //End block
            }

            xml.append("  </building>\n"); //End block
        }
        xml.append("</buildings>");//End main block
        
        //Genereate a XML file from the Stringbuilder xml
        Files.write(Paths.get("output.xml"), xml.toString().getBytes(StandardCharsets.UTF_8));

    }
}

//Classes
class Building {
    String name;
    List<Address> addresses = new ArrayList<>();
    List<Owner> owners = new ArrayList<>();
    List<Company> companies = new ArrayList<>();
}

class Owner {
    String name;
    List<Address> addresses = new ArrayList<>();
    Phone phone;
}

class Company {
    String name;
    String type;
    List<Address> addresses = new ArrayList<>();
    Phone phone;
}

class Address {
    String street;
    String city;
    String zipcode;


    @Override //Override to gain access to the superclass methods equals and hashcode
    //Check equlity based on field
    public boolean equals(Object o) { //Defines when two objects are considered equal
        if (this == o) return true; //Check if they have same reference: equal
        if (!(o instanceof Address)) return false; //If object o is not an Address: not equal
        Address a = (Address) o; //Cast o to an address to compare fields
        return street.equals(a.street) && city.equals(a.city) && zipcode.equals(a.zipcode); //If all fields match they are equal
    }

    @Override //(Hashset recognize them as same object / prevent duplicates)
        public int hashCode() { //If two obejcts are equal they should have the same hashcode aswell 
        return Objects.hash(street, city, zipcode);
    }
}

class Phone {
    String landline;
    String fax;

    @Override 
    //Check equlity based on field
    public boolean equals(Object o) {
        if (this == o) return true; //Check if they have same reference: equal
        if (!(o instanceof Phone)) return false; //If object o is not a Phone: not equal
        Phone p = (Phone) o; //Cast o to an phone to compare fields
        return landline.equals(p.landline) && fax.equals(p.fax); //If all fields match they are equal
    }

    @Override
    public int hashCode() {
        return Objects.hash(landline, fax);
    }
}




