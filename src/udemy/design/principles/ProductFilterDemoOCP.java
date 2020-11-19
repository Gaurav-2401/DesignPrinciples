package udemy.design.principles;// Open Closed Principle + udemy.design.principles.Specification Design Pattern

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

enum Color{
    RED, GREEN, BLUE
}
enum Size{
    SMALL, MEDIUM, LARGE, HUGE
}

class Product{
    public String name;
    public Color color;
    public Size size;
    public Product(String name, Color color, Size size){
        this.name=name;
        this.color=color;
        this.size=size;
    }
}

class ProductFilter{

    public Stream<Product> filterByColor(List<Product> products, Color color){
        return products.stream().filter(p -> p.color == color);
    }
    public Stream<Product> filterBySize(List<Product> products, Size size){
        return products.stream().filter(p -> p.size == size);
    }
    public Stream<Product> filterByColorAndSize(List<Product> products, Color color, Size size){
        return products.stream().filter(p -> p.color == color && p.size == size);
    }

}

interface Specification<T>{
    boolean isSatisfied(T item);
}
interface Filter<T>{
    Stream<T> filter(List<T> items, Specification<T> spec);
}

class ColorSpecification implements Specification<Product>{
    private Color color;

    public ColorSpecification(Color color){
        this.color=color;
    }

    @Override
    public boolean isSatisfied(Product item){
        return item.color==color;
    }

}

class SizeSpecification implements Specification<Product>{
    private Size size;

    public SizeSpecification(Size size){
        this.size=size;
    }

    @Override
    public boolean isSatisfied(Product item){
        return item.size==size;
    }

}

class AndSpecification<T> implements Specification<T>{

    private Specification<T> firstSpec;
    private Specification<T> secondSpec;

    public AndSpecification(Specification<T> firstSpec, Specification<T> secondSpec)
    {
        this.firstSpec=firstSpec;
        this.secondSpec=secondSpec;
    }

    @Override
    public boolean isSatisfied(T item){
        return firstSpec.isSatisfied(item) && secondSpec.isSatisfied(item);
    }
}

class BetterFilter implements Filter<Product>{
    @Override
    public Stream<Product> filter(List<Product> items, Specification<Product> spec){
        return items.stream().filter(p->spec.isSatisfied(p));
    }
}

public class ProductFilterDemoOCP {
    public static void main(String[] args) {
        Product apple = new Product("Apple", Color.GREEN, Size.SMALL);
        Product tree = new Product("Tree", Color.GREEN, Size.LARGE);
        Product house = new Product("House", Color.BLUE, Size.LARGE);

        List<Product> products = new ArrayList<>();
        products.add(apple);
        products.add(tree);
        products.add(house);

        ProductFilter pf = new ProductFilter();
        System.out.println("Green Products (old): ");
        pf.filterByColor(products, Color.GREEN)
                .forEach(p -> System.out.println(" - " + p.name + " is green"));
        // till now everything is fine except we are violating Open Closed Principle as we are modifying the existing code for
        //  adding new filter like udemy.design.principles.Size or let's say price

        //implementing Open Closed Principle  using interfaces
        BetterFilter bf = new BetterFilter();
        System.out.println("Green Products (new): ");
        bf.filter(products, new ColorSpecification(Color.GREEN))
        .forEach(p -> System.out.println(" - " + p.name + " is green"));

        System.out.println("Large Blue items: ");
        bf.filter(products, new AndSpecification<>(new ColorSpecification(Color.BLUE), new SizeSpecification(Size.LARGE)))
                .forEach(p -> System.out.println(" - " + p.name + " is blue and large"));
    }
}
