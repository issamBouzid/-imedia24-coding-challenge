## -imedia24-coding-challenge

#### Approach for task 3 to add a stock information

Adding a **ProductStock** table where we identify the product and provide quantity information as a field.
Saving a new product implies saving its stock information in the **ProductStock** table

we could keep it simple by adding a stock information field to the **Product** table
but the idea was to put all the products stock informations in a separate table

#### Docker file

build the docker image and run the container by :

```bash
> cd the-root-project-folder
> ./gradlew build
> docker build --build-arg SHOP_JAR_FILE=build/libs/shop-0.0.1-SNAPSHOT.jar -t repo/img-name:tag .
> docker run -p 8080:8080 repo/img-name:tag --name shop-imedia24
```
