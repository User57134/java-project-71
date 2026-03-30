.PHONY: clean build dist run-dist


run-dist: dist
	./build/install/app/bin/app


dist:
	./gradlew installDist


build: clean
	./gradlew build


clean:
	./gradlew clean
