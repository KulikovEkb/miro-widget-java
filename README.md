# Widget service

A web service to work with widgets on a board via HTTP REST API.The service stores only widgets and assuming that all clients work with the same board.

## Glossary
A Widget is an object on a plane in a Cartesian coordinate system that has coordinates (X, Y),
Z-index, width, height, last modification date, and a unique identifier.
</br>X, Y, and Z-index are
integers (may be negative).
</br> Width and height are integers > 0.
</br>Widget attributes should be not null.
</br>A Z-index is a unique sequence common to all widgets that
determines the order of widgets (regardless of their coordinates).
Gaps are allowed. The higher the value, the higher the widget
lies on the plane.

## Run
There are two implementations of repository – in-memory and H2 SQL Database.
</br>By default in-memory implementation is used.
</br>To change default implementation to H2 uncomment variable
`spring.profiles.active` in `application.properties` file and rerun.
