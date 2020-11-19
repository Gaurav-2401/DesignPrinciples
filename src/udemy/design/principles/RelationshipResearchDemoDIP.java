package udemy.design.principles;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.javatuples.Triplet;

enum Relationship{
    PARENT,
    CHILD,
    SIBLING
}

class Person
{
    public String name;
    public Person(String name){
        this.name=name;
    }
}

interface RelationshipBrowser{
    List<Person> findAllChildrenOf(String name);
}

// Relationships class is a low-level module because it's related to data storage.
// It provides/keeps a list for manipulation and provides it using getter method. Doesn't have any business logic
// only single responsibility of this class  is to manipulate the list (also an example of SRP class)
class Relationships implements RelationshipBrowser
{
    private List<Triplet<Person, Relationship, Person>> relations = new ArrayList<>();

    public List<Triplet<Person, Relationship, Person>> getRelations() {
        return relations;
    }

    public void addParentAndChildRelationship(Person person, Person child)
    {
        relations.add(new Triplet<>(person, Relationship.PARENT, child));
        relations.add(new Triplet<>(child, Relationship.CHILD, person));
    }

    @Override
    public List<Person> findAllChildrenOf(String name) {
        return relations.stream()
                .filter(x-> Objects.equals(x.getValue0().name, name) && x.getValue1() == Relationship.PARENT)
                .map(Triplet::getValue2)
                .collect(Collectors.toList());
    }
}

// class Research is a high level module because it allows us to perform operations on those low level constructs, the end user
// doesn't doesn't care about how data is stored etc, they care about actual research i.e. data about who is related to whom.
// here we see in the constructor, the high level module Research is dependent on low level module Relationships
// defined in constructor and hence violates dependency inversion principle. To fix this issue, we follow the second
// point which says we should instead depend on abstractions and hence will use interface
// we define interface RelationshipBrowser and Relationships class implements the interface and the operation on list of relations
// will now happen in Relationships class (low level module), the benefit is if we want to change anything like instead of list
// we want to return array we can easily do it in low level module with least amount of changes
// now after adding abstraction you can see in research constructor we are not dependent on Relationship low level module instead
// we are dependent on abstractions and hence we are not violating DIP. Now we can make any changes to low level module like instead
// of using triplets for relationships we can use class as well and that change can be made easily.Earlier, if we would have made that
// change, we would have to make change in high level module Research as well because we were using Relationship low level module
// directly in Research

class Research{
//    public Research(Relationships relationships){
//        List<Triplet<Person, Relationship, Person>> relations = relationships.getRelations();
//        relations.stream()
//                .filter(x->x.getValue0().name.equals("John") && x.getValue1() == Relationship.PARENT)
//                .forEach(ch -> System.out.println("John has a child called " + ch.getValue2().name));
//    }

    public Research(RelationshipBrowser browser){
        List<Person> children = browser.findAllChildrenOf("John");
        for(Person child : children){
            System.out.println("John has a child called "+child.name);
        }
    }
}


public class RelationshipResearchDemoDIP {
    public static void main(String[] args) {

        Person parent = new Person("John");
        Person child1 = new Person("Chris");
        Person child2 = new Person("Mary");

        Relationships relationships = new Relationships();
        relationships.addParentAndChildRelationship(parent, child1);
        relationships.addParentAndChildRelationship(parent, child2);

        new Research(relationships);
    }
}
