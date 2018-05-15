deps:
	wget -O lib/spec.alpha-0.1.143.jar https://repo1.maven.org/maven2/org/clojure/spec.alpha/0.1.143/spec.alpha-0.1.143.jar
	wget -O lib/core.specs.alpha-0.1.24.jar https://repo1.maven.org/maven2/org/clojure/core.specs.alpha/0.1.24/core.specs.alpha-0.1.24.jar
	wget -O lib/clojure-1.9.0.jar https://repo1.maven.org/maven2/org/clojure/clojure/1.9.0/clojure-1.9.0.jar

compile:
	java -cp .:./lib/core.specs.alpha-0.1.24.jar:./lib/spec.alpha-0.1.143.jar:./lib/clojure-1.9.0.jar:./src -Dclojure.compile.path=build clojure.lang.Compile main

run:
	java -cp .:./lib/core.specs.alpha-0.1.24.jar:./lib/spec.alpha-0.1.143.jar:./lib/clojure-1.9.0.jar:./build main

src-run:
	java -cp .:./lib/core.specs.alpha-0.1.24.jar:./lib/spec.alpha-0.1.143.jar:./lib/clojure-1.9.0.jar:./src clojure.main src/main.clj
