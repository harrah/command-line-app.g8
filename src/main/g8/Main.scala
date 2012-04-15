package $package$

   import sbt._

	// parsing + tab completion combinators
	import complete._
	import DefaultParsers._

final class Main extends xsbti.AppMain
{
   /** Defines the entry point for the application.
   * The call to `initialState` sets up the application.
   * The call to runLogged starts command processing.
	*/
   def run(configuration: xsbti.AppConfiguration): xsbti.MainResult =
      MainLoop.runLogged( initialState(configuration) )

   /** Sets up the application by constructing an initial State instance with the 
	* supported commands and initial commands to run.
	*
	* http://harrah.github.com/xsbt/latest/api/sbt/State%24.html
	*/
   def initialState(configuration: xsbti.AppConfiguration): State =
   {
			// These are the commands that the application supports.
      val commandDefinitions = hello +: BasicCommands.allBasicCommands

			// These are the commands that are run when the application starts up.
			// "iflast shell" will drop to the interactive prompt if no arguments are
			//    provided on the command line
      val commandsToRun =
			Hello +: "iflast shell" +: configuration.arguments.map(_.trim)

      State( configuration, commandDefinitions, Set.empty,
			None, commandsToRun, State.newHistory,
         AttributeMap.empty, initialGlobalLogging, State.Continue )
   }

	/** A sample command that says hello to its argument.
	* The first argument to Command provides the command name.
	* The second is a function State => Parser[T], providing the parser+tab completion.
	* The third argument is a function (State, T) => State that accepts the result of parsing
	*  and transforms the application state (that is, it does the work).
	*
	* See also https://github.com/harrah/xsbt/wiki/Commands
	*/
	def hello = Command(Hello)(state => helloParser)( helloAction )

   def Hello = "hello"

	def helloParser: Parser[String] =
		token(Space ~> NotSpace, "<name>")

	def helloAction(state: State, name: String): State = {
      s.log.info( "Hello %s!".format(name) )
      s
   }
		
   /** Configures logging to log to a temporary backing file as well as to the console. 
   * An application would need to do more here to customize the logging level and
   * provide access to the backing file (like sbt's last command and logLevel setting).*/
   def initialGlobalLogging: GlobalLogging =
      GlobalLogging.initial(MainLogging.globalDefault _, java.io.File.createTempFile("app", "log"))
}