default:
	cat Makefile

test:
	mvn test

deploy:
	echo "I hope you bumped the version number!"
	git push
	mvn test deploy -X
