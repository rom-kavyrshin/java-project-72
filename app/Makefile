build:
	./gradlew installDist
run-dist:
	./build/install/app/bin/app
checkstyleMain:
	./gradlew checkstyleMain
checkstyleTest:
	./gradlew checkstyleTest
checkstyle: checkstyleMain checkstyleTest

test:
	./gradlew cleanTest test
report:
	./gradlew jacocoTestReport

.PHONY: build