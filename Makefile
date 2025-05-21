clean:
	make -C app clean

install:
	make -C app install

build:
	make -C app build

dev:
	make -C app dev

checkstyle:
	make -C app checkstyle

test:
	make -C app test

report:
	make -C app report

image-build:
	docker build -t rom-kavyrshin/java-project-72 -f Dockerfile .

image-run:
	docker run -p 8080:7070 --env-file local.env rom-kavyrshin/java-project-72

image-run-it:
	docker run -it -p 8080:7070 --env-file local.env rom-kavyrshin/java-project-72 bash
