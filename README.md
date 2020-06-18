# TrafficSimulator-TP2
Repositorio de la asignatura de Tecnología de la Programación 2 (TP2) de la Facultad de Informática (FdI) de la Universidad Complutense de Madrid (UCM) del curso 2017-2018.

## Instrucciones de ejecución

Para poder ejecutar el programa es necesario pasarle ciertos argumentos.

usage: Main [-h] [-i \<arg\>] [-m \<arg\>] [-o \<arg\>] [-t \<arg\>]<br>
-h, --help Prints help<br>
-i, --input \<arg\> Events input file<br>
-m,--mode \<arg\> ’batch’ for batch mode and ’gui’ for GUI mode (default value is ’batch’)<br>
-o,--output \<arg\> Output file, where reports are written.<br>
-t,--time \<arg\> Time units to execute the simulator’s main loop (default value is 10).<br>


Ejemplos de argumentos para la ejecución.
		
- -i resources/examples/events/basic/ex1.ini<br>
- -i resources/examples/events/advanced/ex1.ini -t 100<br>
- -m batch -i resources/examples/events/basic/ex1.ini -t 20<br>
- -m batch -i resources/examples/events/advanced/ex1.ini<br>
- -m gui -i resources/examples/events/basic/ex1.ini<br>
- -m gui -i resources/examples/events/advanced/ex1.ini<br>
- --help
