KC=	kotlinc
KFLAG=	-cp
GLIB=	ve/usb/libGrafo

all:	libGrafoKt.jar

libGrafoKt.jar:$(GLIB)/Lado.class\
		$(GLIB)/Arco.class\
		$(GLIB)/ArcoCosto.class\
		$(GLIB)/Grafo.class\
		$(GLIB)/GrafoDirigidoCosto.class\
		$(GLIB)/VerticeDijkstra.class\
		$(GLIB)/CCM_Dijkstra.class\

	jar -cvf $@ $(GLIB)/*.class 

$(GLIB)/Lado.class: $(GLIB)/Lado.kt
	$(KC) $(GLIB)/Lado.kt

$(GLIB)/Arco.class: $(GLIB)/Arco.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/Arco.kt

$(GLIB)/ArcoCosto.class: $(GLIB)/ArcoCosto.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/ArcoCosto.kt

$(GLIB)/Grafo.class: $(GLIB)/Grafo.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/Grafo.kt

$(GLIB)/GrafoDirigidoCosto.class: $(GLIB)/GrafoDirigidoCosto.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/GrafoDirigidoCosto.kt

$(GLIB)/VerticeDijkstra.class: $(GLIB)/VerticeDijkstra.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/VerticeDijkstra.kt

$(GLIB)/CCM_Dijkstra.class: $(GLIB)/CCM_Dijkstra.kt
	$(KC) $(KFLAG) $(GLIB): $(GLIB)/CCM_Dijkstra.kt

clean:
	rm -rf libGrafoKt.jar META-INF $(GLIB)/*.class
