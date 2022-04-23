# apotheke_aufgabe

Postman values:

GET URL(all Products) localhost:8083/getAllProducts

________________________________________________________________

DELETE URL (delete product) localhost:8083/deleteProduct/10
________________________________________________________________

PUT URL (edit) localhost:8083/wasser/edit

JSON Body

    "produktname": "Aspirin",
    "wirkstoff": "Acetylsalicylsäure",
    "hersteller": "Bayer",
    "anzahl": 1
	
________________________________________________________________

PUT URL (order From Store) localhost/orderFromStore/Aspirin,1)

________________________________________________________________


PUT URL (order More) localhost/orderMore/Tablette,1)

________________________________________________________________


POST URL (add Product)localhost:8083/addProduct

JSON Body

    "produktname": "Aspirin",
    "wirkstoff": "Acetylsalicylsäure",
    "hersteller": "Bayer",
    "anzahl": 5
________________________________________________________________

GET URL (one product) localhost:8083/getProduct/Aspirin

