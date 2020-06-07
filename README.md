DRON-DELIVERY
This project is the implementation of a dron delivery system with a input of instructions
using files.

INITIAL CONSIDERATIONS
- Each file correspond to a set of maximum three instructions.
- Each time that Dron finish a delivery, continue with next one. So instructions must be done
to guide it in this way.
- The system must be compatible with different types of Dron.
- The instruction source can change in the future.
- The range of delivery is 10 blocks around. In case that a delivery exceed this value 
the instruction file must be omitted.
- Though there is not database, there should exist a mechanism that let to store until 
session finishes.
- Position changes are the same for all kind of Drons.

ENTITIES
In this project entities are data classes managed by repositories. According to the context
the necessary entities are: Dron, Route and parametric values (these are only constants there).

PATTERNS:
- The main pattern is singleton. Most of the objects are singleton. In this way we avoid to create 
an instance each time a component need other. Each object declares its dependencies, even It is not necessary, 
it makes more clear the dependencies of an object.

- In order to decouple and be able to adapt to various types of Dron the Dron Component use Command pattern.
By way of example in case where It is necessary to add a new Dron Type, adding a Dron Executor implementation is enough.

- Repository Layer uses template pattern for solving the absence of databases.

MAIN COMPONENTS

- Repository: Responsible for managing and storing entity information.
- Dron Driver: Set of objects that manage the interaction with Drons. Know the 
- Reader: Get the instructions from source. In case that source change
this new source reader must implement the interface.
- Service: This object interacts with repositories and dron drive for tracking, saving 
and updating the entities with each command execution.
- Manager: Manage the whole  delivery process, execute validation and generate final reports.

FUTURE IMPROVEMENTS

- Many more unit test cases.
- Consider execute the delivery per Dron using async way.
- Implement Dron executor async.
- Implement route calculator and recalculate commands when a delivery is out of range.
- Much more ...........









