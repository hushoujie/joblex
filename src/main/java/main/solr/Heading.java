package main.solr;

public class Heading implements Comparable {

    private final String name;
    private final String id;

    public Heading(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    @Override
    public int compareTo(Object o) {
        return ((Heading) o).getName().compareTo(name);
    }

}
