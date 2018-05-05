/**
 * @author cork
 *
 */
package com.ib.scalaib

import java.io.PrintWriter
import java.text.SimpleDateFormat
// import java.util.Date
import org.joda.time._

import scala.collection.mutable.{ HashMap, ArrayBuffer }
import scala.concurrent._
import scala.concurrent.Future
import scala.concurrent.Promise
import scala.concurrent.Await
import scala.concurrent.duration._

import com.ib.client.CommissionReport
import com.ib.client.Contract
import com.ib.client.ContractDetails
import com.ib.client.EClientSocket
import com.ib.client.EWrapper
import com.ib.client.Execution
import com.ib.client.Order
import com.ib.client.OrderState
import com.ib.client.TickType
//import com.ib.client.UnderComp

import rx.lang.scala.Observable
import rx.lang.scala.Observer
import rx.lang.scala.Subscription

import IbDataTypes._

/**
 * Principal connection class
 * User facing methods are here
 * This also provides the interfaces for the callback functions that the IB EClientSocket requires through
 * the EWrapper and EWrapperTrait traits
 *
 * @constructor Only has a single constructor without parameters.
 *
 */
class IbConnection extends EWrapper
  with OpenOrders with IbPromise with MktScanner with MarketTick with MarketDepth
  with RealTimeBars with Orders with HistoricalData with IbContractDetails
  with Quickies with Errors with UnimplementedStubs with Account {

  /**
   * As seen from class IbConnection, the missing signatures are as follows.
   *  For convenience, these are usable as stub implementations.
   */
  def accountSummary(x$1: Int, x$2: String, x$3: String, x$4: String, x$5: String): Unit = ???
  def accountSummaryEnd(x$1: Int): Unit = ???
  def accountUpdateMulti(x$1: Int, x$2: String, x$3: String, x$4: String, x$5: String, x$6: String): Unit = ???
  def accountUpdateMultiEnd(x$1: Int): Unit = ???
  def connectAck(): Unit = ???
  def deltaNeutralValidation(x$1: Int, x$2: com.ib.client.DeltaNeutralContract): Unit = ???
  def displayGroupList(x$1: Int, x$2: String): Unit = ???
  def displayGroupUpdated(x$1: Int, x$2: String): Unit = ???
  def orderStatus(x$1: Int, x$2: String, x$3: Double, x$4: Double, x$5: Double, x$6: Int, x$7: Int, x$8: Double, x$9: Int, x$10: String): Unit = ???
  def position(x$1: String, x$2: com.ib.client.Contract, x$3: Double, x$4: Double): Unit = ???
  def positionEnd(): Unit = ???
  def positionMulti(x$1: Int, x$2: String, x$3: String, x$4: com.ib.client.Contract, x$5: Double, x$6: Double): Unit = ???
  def positionMultiEnd(x$1: Int): Unit = ???
  def softDollarTiers(x$1: Int, x$2: Array[com.ib.client.SoftDollarTier]): Unit = ???
  def updatePortfolio(x$1: com.ib.client.Contract, x$2: Double, x$3: Double, x$4: Double, x$5: Double, x$6: Double, x$7: Double, x$8: String): Unit = ???
  def verifyAndAuthCompleted(x$1: Boolean, x$2: String): Unit = ???
  def verifyAndAuthMessageAPI(x$1: String, x$2: String): Unit = ???
  def verifyCompleted(x$1: Boolean, x$2: String): Unit = ???
  def verifyMessageAPI(x$1: String): Unit = ???

  def familyCodes(x$1: Array[com.ib.client.FamilyCode]): Unit = ???
  def headTimestamp(x$1: Int,x$2: String): Unit = ???
  def histogramData(x$1: Int,x$2: java.util.List[com.ib.client.HistogramEntry]): Unit = ???
  def historicalData(x$1: Int,x$2: com.ib.client.Bar): Unit = ???
  def historicalDataEnd(x$1: Int,x$2: String,x$3: String): Unit = ???
  def historicalDataUpdate(x$1: Int,x$2: com.ib.client.Bar): Unit = ???
  def historicalNews(x$1: Int,x$2: String,x$3: String,x$4: String,x$5: String): Unit = ???
  def historicalNewsEnd(x$1: Int,x$2: Boolean): Unit = ???
  def historicalTicks(x$1: Int,x$2: java.util.List[com.ib.client.HistoricalTick],x$3: Boolean): Unit = ???
  def historicalTicksBidAsk(x$1: Int,x$2: java.util.List[com.ib.client.HistoricalTickBidAsk],x$3: Boolean): Unit = ???
  def historicalTicksLast(x$1: Int,x$2: java.util.List[com.ib.client.HistoricalTickLast],x$3: Boolean): Unit = ???
  def marketRule(x$1: Int,x$2: Array[com.ib.client.PriceIncrement]): Unit = ???
  def mktDepthExchanges(x$1: Array[com.ib.client.DepthMktDataDescription]): Unit = ???
  def newsArticle(x$1: Int,x$2: Int,x$3: String): Unit = ???
  def newsProviders(x$1: Array[com.ib.client.NewsProvider]): Unit = ???
  def orderStatus(x$1: Int,x$2: String,x$3: Double,x$4: Double,x$5: Double,x$6: Int,x$7: Int,x$8: Double,x$9: Int,x$10: String,x$11: Double): Unit = ???
  def pnl(x$1: Int,x$2: Double,x$3: Double,x$4: Double): Unit = ???
  def pnlSingle(x$1: Int,x$2: Int,x$3: Double,x$4: Double,x$5: Double,x$6: Double): Unit = ???
  def rerouteMktDataReq(x$1: Int,x$2: Int,x$3: String): Unit = ???
  def rerouteMktDepthReq(x$1: Int,x$2: Int,x$3: String): Unit = ???
  def smartComponents(x$1: Int,x$2: java.util.Map[Integer,java.util.Map.Entry[String,Character]]): Unit = ???
  def symbolSamples(x$1: Int,x$2: Array[com.ib.client.ContractDescription]): Unit = ???
  def tickByTickAllLast(x$1: Int,x$2: Int,x$3: Long,x$4: Double,x$5: Int,x$6: com.ib.client.TickAttr,x$7: String,x$8: String): Unit = ???
  def tickByTickBidAsk(x$1: Int,x$2: Long,x$3: Double,x$4: Double,x$5: Int,x$6: Int,x$7: com.ib.client.TickAttr): Unit = ???
  def tickByTickMidPoint(x$1: Int,x$2: Long,x$3: Double): Unit = ???
  def tickNews(x$1: Int,x$2: Long,x$3: String,x$4: String,x$5: String,x$6: String): Unit = ???
  def tickPrice(x$1: Int,x$2: Int,x$3: Double,x$4: com.ib.client.TickAttr): Unit = ???
  def tickReqParams(x$1: Int,x$2: Double,x$3: String,x$4: Int): Unit = ???  
  def securityDefinitionOptionalParameterEnd(x$1: Int): Unit = ???

  def securityDefinitionOptionalParameter(x$1: Int, x$2: String, x$3: Int, x$4: String, x$5: String,
    //
    x$6: java.util.Set[String], x$7: java.util.Set[java.lang.Double]): Unit =
    ???

  import Utils._

  private val JTS_PORT: Int = 7496
  private val LOCALHOST: String = "127.0.0.1"

  private var reqId = 999
  /** Advance and return a new request ID */
  def getNextReqId: Int = { reqId += 1; reqId }

  private var nextValidOrderId = -1

  /**
   * Advance and return a new Order ID
   *
   * @note Since we start with a valid one (provided by TWS) use that one first
   */
  def getNextOrderId: Int = { nextValidOrderId += 1; nextValidOrderId - 1 }

  // main client
  val client: EClientSocket = new EClientSocket(IbConnection.this, ???); // XXX

  private var connectId = 0

  type NextValidOrderIdFunc = (Int) => Unit

  var captureNextValidOrderId: NextValidOrderIdFunc = (n: Int) => Unit

  /** TWS callback */
  // NOTE - this sets the Order ID - distinct from our generated Request Ids
  def nextValidId(orderId: Int) {
    traceln(s"nextValidOrderId(from TWS): $orderId");
    captureNextValidOrderId(orderId)
  }

  val INFINITE = scala.concurrent.duration.Duration.Inf
  /*
   *  ----------------------------------------------
   *  
   *  User accessible methods - the primary exports
   *  
   *  ----------------------------------------------
   */

  /**
   * Performs the connection to the TWS (server) application
   *
   * @param connectId (Default=1) Identifies a single connection.
   * @param port      (Default=7496) Port for the socket connection.
   * @note  The SimpleConnecion object supports a single connection to the server, identified by the passed ID.
   */
  def connect(connctId: Int = 1, port: Int = 7496, serverLogLevel: Int = 1): Boolean = {
    import ExecutionContext.Implicits.global

    connectId = connctId
    var bOk = true
    // TODO: -1 ?? will this work - needs a good test
    def setErrorHandler = setGenericErrorHandler(-1, "connect(" + connctId + "): ", (err: Int) => { if (err != 504) bOk = false })

    // this will block, but !!
    def waitOnOpenOrders(waitTime: Int) = {
      if (bOk) {
        traceln("wait on OpenOrders - enter")
        val p1 = Promise[Boolean]()
        // when openOrderEnd has been called, signalComplete will be called
        setSignalComplete(() => { p1.success(true); Unit })
        // timer, a few seconds
        val timeout = Future[Boolean] { Thread.sleep(waitTime); true }
        setOpenOrderHandler(openOrderHandler)
        // TODO: I'm sure there's a better way to do this ...
        val b = Await.result(Future.firstCompletedOf(List(p1.future, timeout)), INFINITE)
        resetOpenOrderHandler
        setSignalComplete(() => {})
        traceln("wait on OpenOrders - exit")
      }
    }

    // main block
    try {
      captureNextValidOrderId = (nxtOrderId: Int) => {
        nextValidOrderId = nxtOrderId;
        println(s"Captured NextValidOrderId: $nextValidOrderId");
        Unit
      }
      // set the server log level
      client.setServerLogLevel(serverLogLevel)

      // set error handler just before connect
      setErrorHandler

      // the eConnect call returns no status ?!?
      client.eConnect(LOCALHOST, port, connectId)

      // right after TWS connects it will send us its list of open orders
      // get our handler into place, and wait for it ...
      waitOnOpenOrders(3000 /* ms to wait */ )

      // set the order status handler ( defined in OpenOrders )
      setOrderStatusHandler(orderStatusHandlerForFutures)

      // need to call isConnected to determine success
      if (client.isConnected()) {
        traceln("Connected!!")
        true
      } else {
        traceln("ERROR - NOT Connected !! ")
        false
      }
    } catch {
      case e: Exception =>
        traceln("Connect Exception: " + e.toString())
        false
    }
  }

  /**
   * Disconnects from the TWS server
   *
   */
  def disconnect() = {
    errorsFor = "disconnect(): "
    client.eDisconnect()
    traceln("Dis-Connected(" + connectId + ")")
  }

  def connectionClosed(): Unit = {
    traceln("Connection closed.")
  }

} // clas IbConnection  

trait UnimplementedStubs extends EWrapper {

  import Utils._

  // Members declared in com.ib.client.EWrapper

  /** TWS callback */
  def currentTime(millis: Long) {
    traceln("currentTime: " + millis);
  }

  /** TWS callback */
  def fundamentalData(reqId: Int, data: String) {
    traceln("fundamentalData");
  }

  /** TWS callback */
  //    def deltaNeutralValidation(reqId: Int, underComp: UnderComp) { // XXX
  //        traceln("deltaNeutralValidation");
  //    }

  /** TWS callback */
  def marketDataType(reqId: Int, marketDataType: Int) {
    traceln("marketDataType");
  }

  /** TWS callback */
  def commissionReport(commissionReport: CommissionReport) {
    traceln("commissionReport");
  }

  /** TWS callback */
  def execDetails(reqId: Int, contract: Contract, execution: Execution) {
    traceln("execDetails");
  }

  /** TWS callback */
  def execDetailsEnd(reqId: Int) {
    traceln("execDetailsEnd");
  }

  /** TWS callback */
  def updateNewsBulletin(msgId: Int, msgType: Int, message: String, origExchange: String) {
    traceln("updateNewsBulletin");
  }

  /** TWS callback */
  def managedAccounts(accountsList: String) {
    traceln("managedAccounts");
  }

  /** TWS callback */
  def receiveFA(faDataType: Int, xml: String) {
    traceln("receiveFA");
  }

}
