# Explicación detallada de como la actividad fue resuelta

En primer lugar se tiene que, las intersecciones que conectan las calles de la ciudad de *CaracasSur* se representan como los vértices de un digrafo, y las calles son los lados que conectan cada uno de estos vértices. Así, el tiempo que le toma a Tom cruzar alguna calle cuando no tiene nieve es el costo de los lados. Para lo anterior, se modifico `GrafoDirigidoCosto` de modo que en vez de añadir un solo lado $$(u, v)$$ también agrega el lado $$(v, u)$$ al digrafo ya que los caminos son bidireccionales; el identificador que corresponde a la calle $$(u,v)$$ viene dado por la posición $$i$$ en la que aparece en el archivo de entrada, estos identificadores son almacenados en un matriz de tamaño $$n\times n$$ dónde $$n$$ es el número de intersecciones de la ciudad, por lo que se añade una nueva función a `GrafoDirigidoCosto` llamada `id` que dado los vértices $$u$$ y $$v$$ de algún lado (directo) retorna la entrada $$id_{uv}$$ de la matriz ya descrita. Para los cronogramas de limpiezas planificados por la alcaldía de *CaracasSur* en las calles se tiene que estos son almacenados en un arreglo de tamaño $$m$$ dónde $$m$$ es el número de calles de la ciudad, la $$i$$-énesima posición de este arreglo contiene un `ArrayList` de pares de enteros donde se almacena el cronograma de limpieza de la calle con $$id$$ $$i$$. El primer atributo de algún algún par es el tiempo $$t_i$$ que indica cuando comienza la limpieza de la calle y el segundo atributo es el tiempo $$t_f$$ de finalización.

Para hallar el camino de costo mínimo desde la intersección donde esta la casa de Tom hasta la intersección donde esta el estadio se uso el algoritmo de *Dijkstra*, cuya modificación relevante es la añadidura de una función `w` que dado algún lado del grafo retorna el costo del lado, es decir el tiempo que le toma a Tom cruzar la calle tomando en cuenta el tiempo desde el inicio de la nevada, el cronograma de limpieza de la calle y que mientras la calle se esta limpiando no puede haber un carro. Además, dado que cuando comienza a nevar, el tiempo en cruzar una calle es mayor y viene dado por la función:

$$min((\lceil (1 + \frac{T}{100})\rceil * c_i), 100500 * c_i)$$, donde $$c_i$$ es el tiempo en cruzar la calle cuando no tiene nieve y $$T$$ es el tiempo que lleva recibiendo nieve desde el la última vez que fue limpiada. El subindice $$i$$ se refiere a la calle con $$id$$ $$i$$. Entonces:

Se tiene tiene que los pasos para el cálculo del costo son los siguientes:

1. Se ordena en forma creciente el cronograma de limpieza de la calle de entrada.
2. Se busca el $$t_f$$ que indica la última vez que se limpio la calle, tomando como referencia el tiempo de recorrido desde el inicio de la nevada.
3. Se calcula el tiempo en cruzar la calle, en este caso $$T = t - t_f$$ donde $$t$$ es el tiempo de recorrido desde el inicio de la nevada.
4. Se verifica si hay más limpiezas programadas a partir del par con el tiempo $$t_f$$ del paso 2, en caso de que no, se retorna el tiempo calculado en el paso 3, de lo contrario se realiza el siguiente paso.
5. Se calcula el nuevo tiempo en cruzar la calle el cual viene dado por $$t_{f_k} - t + c_i$$, donde $$t_{f_k}$$ es el tiempo de finalización de la $$k$$-énesima limpieza cuyo $$t_f$$ es mayor o igual al de la limpieza encontrada en el paso 2. Lo anterior se hace mientras el tiempo en cruzar la calle más el tiempo desde el inicio de la nevada interfiera con alguna limpieza.

"Interfiera con alguna limpieza" se refiere al hecho de que dicho tiempo este en algún intervalo de limpieza posterior o que sea mayor que el tiempo de finalización de la misma.

Con lo ya descrito, el atributo $$d$$ de algún vértice del grafo es el tiempo en llegar hasta ese vértice desde el vértice fuente (la casa de Tom) en particular el vértice que representa la intersección dónde esta el estadio. Por lo que, basta con llamar a la función `costo` con tal vértice como entrada y para el obtener el camino se llama a la función `obtenerCaminoDeCostoMinimo` con el mismo parámetro. 