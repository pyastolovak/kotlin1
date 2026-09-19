//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val kotlinBook = Product(
        id = 1,
        name = "Kotlin in Action",
        price = 1_500.0,
        category = Category.BOOKS,
    )

    val coffee = Product(
        id = 2,
        name = "Coffee",
        price = 500.0,
        category = Category.FOOD,
    )

    val headphones = Product(
        id = 3,
        name = "Headphones",
        price = 8_000.0,
        category = Category.ELECTRONICS,
    )

    val customer = Customer(
        name = "Иван",
        email = "ivan@example.com",
        discount = 0.10,
    )

    val orders = listOf(
        Order(
            id = 1,
            customer = customer,
            items = listOf(
                OrderItem(kotlinBook, count = 2),
                OrderItem(coffee, count = 1),
            ),
            status = OrderStatus.Paid(
                transactionId = "TX-123",
            ),
        ),
    )

    println(orders.toReceipt())
}

data class Product (
    val id: Int,
    val name: String,
    val price: Double,
    val category: Category,
)

fun Product.toReceipt(): String {
    val r_name: String
    if (this.name == "Kotlin in Action") {
        r_name = "Книга \"Kotlin в действии\""
    } else if (this.name == "Coffee") {
        r_name = "Кофе"
    } else {
        r_name = "Наушники"
    }
    return r_name
}

data class Customer (
    val name: String,
    val email: String,
    val discount: Double,
)

data class Order (
    val id: Int,
    val customer: Customer,
    val items: List<OrderItem>,
    val status: OrderStatus,
)

fun Order.toReceipt(): String {
    var totalPrice: Double = 0.0
    for (item in items) {
        totalPrice = totalPrice + item.product.price * item.count
    }
    val totalDiscount = totalPrice * customer.discount
    val totalWithDiscount = totalPrice - totalDiscount
    return buildString {
        appendLine("Заказ #$id")
        appendLine("Покупатель: ${customer.name} <${customer.email}>")
        appendLine("Статус: ${status.toReceipt()}")
        appendLine()
        for (item in items) {
            val totalItem = item.product.price * item.count
            appendLine("${item.product.toReceipt()} * ${item.count} = $totalItem")
        }
        appendLine()
        appendLine("Скидка: ${customer.discount * 100}")
        appendLine("Итого: $totalWithDiscount")
    }
}

fun List<Order>.toReceipt(): String {
    var result: String = ""
    for (order in this) {
        result = result + order.toReceipt()
        result = result + "\n\n"
    }
    return result
}

data class OrderItem (
    val product: Product,
    val count: Int,
)

sealed class OrderStatus {
    data object Created: OrderStatus()
    data class Paid (
        val transactionId: String,
    ): OrderStatus()
    data class Canceled (
        val reason: String
    ): OrderStatus()
    data object Delivered: OrderStatus()
}

fun OrderStatus.toReceipt(): String {
    if (this == OrderStatus.Created) {
        return "Создан"
    } else if (this is OrderStatus.Paid) {
        return "Оплачен, транзакция: ${this.transactionId}"
    } else if (this is OrderStatus.Canceled) {
        return "Отменён, причина: ${this.reason}"
    } else {
        return "Доставлен"
    }
}

enum class Category{
    FOOD,
    ELECTRONICS,
    BOOKS,
    OTHER
}