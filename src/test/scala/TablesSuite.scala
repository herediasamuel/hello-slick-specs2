import org.specs2._
import scala.slick.driver.H2Driver.simple._
import scala.slick.jdbc.meta._

class TablesSuite extends mutable.Specification {

  val suppliers = TableQuery[Suppliers]
  val coffees   = TableQuery[Coffees]
  
  def createSchema(implicit session: Session) = {
    if (MTable.getTables(suppliers.baseTableRow.tableName).list.isEmpty)
      suppliers.schema.create
    if (MTable.getTables(coffees.baseTableRow.tableName).list.isEmpty)
      coffees.schema.create
  }
  
  def insertSupplier(implicit session: Session) = 
    suppliers += (101, "Acme, Inc.", "99 Market Street", "Groundsville", "CA", "95199")
  
  trait _Session extends specification.Scope with mutable.After {
    println("Opening session")
    val tableName = s"test${util.Random.nextInt}"
    implicit val session = Database.forURL(s"jdbc:h2:mem:$tableName", driver = "org.h2.Driver").createSession()
    println(s"...session: $session")

    println("Creating schema")
    createSchema(session)

    def after = {
      println("Closing session")
      session.close()
    }
  }
  
  "Slick" should {
    "create schema" in new _Session {
      val tables = MTable.getTables.list
      assert(tables.size == 2)
      assert(tables.count(_.name.name.equalsIgnoreCase("suppliers")) == 1)
      assert(tables.count(_.name.name.equalsIgnoreCase("coffees")) == 1)
    }    
    "insert a supplier" in new _Session {
      val insertCount = insertSupplier(session)
      assert(insertCount == 1)
    }
    "query Suppliers" in new _Session {
      insertSupplier(session)
      val results = suppliers.list
      assert(results.size == 1)
      assert(results.head._1 == 101)
    }
  }
}