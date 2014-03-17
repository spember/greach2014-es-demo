package pember.greach3.eventsourcing

/**
 *
 * @author spember
 *
 * This is a shoddy representation of a Product do go in our store
 *
 * */
class Product {

    static List sizes = ['XXL', 'XL', 'L', 'M', 'S', 'XS']

    String sku
    // Assume USD only, for demo purposes
    BigDecimal price
    String title
    String description
    String size

    String imageUrl

    static constraints = {
        sku blank: false, nullable: false, maxSize: 18, unique: true
        title blank: false, nullable: false, maxSize: 100
        description blank: false, nullable: false, maxSize: 4096
        size blank: false, nullable: false, inList: sizes
        price blank: false, nullable: false, min: (BigDecimal)(0)
    }

    String toString() {
        "$title($sku)"
    }

}
