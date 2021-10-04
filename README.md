# receiptfox - receipt creation, management and APIs

This is a small microservice that can generate beautiful looking invoice documents as PDF for you. It also manages your invoices and saves them within a postgres database. You can
control everything by REST APIs. This service can be used as part of SaaS billing.

This is open source software by [encircle360](https://encircle360.com). Use on your own risk and for personal use.

## Dependencies

Running receiptfox properly needs some small dependencies, you need an S3 compatible storage, docsrabbit running in background and a postgres database.

But don't be afraid, you can start everything with one simple command:

```docker-compose up -d```

After setting up your dependencies, start receiptfox like this:

```docker run -d -p8080:8080 registry.gitlab.com/encircle360-oss/receiptfox:latest```

## Organization units

First you need at least one organization unit, this can be your company itself or one department. This unit contains information about your companies address, the receipt number
pattern and a default template id for receipts.

A sample post request body for creating an organization unit you can post to ```/organization-units```:

```
{
	"name": "test",
	"address": {
		"company": "Example company",
		"firstName": null,
		"lastName": null,
		"street": null,
		"houseNumber": null,
		"postalCode": null,
		"city": null,
		"countryCode": null
	},
	"organizationImage": "==0",
	"receiptNumberPattern": "XXX",
	"defaultTemplateId": "615ac02053166e59daa57f34"
}
```

## Template mappings

To use the correct template everytime, you can use template mappings.

A sample post request body for ```/template-mappings``` looks like this:

```
{
	"templateId": "615ac02053166e59daa57f34",
	"type": "INVOICE",
	"organizationUnitId": 1,
	"description": null,
	"standart": true
}
```

## Receipt creation

For example: you can create invoices with a simple post request to the ```/receipts``` endpoint.

The request body should look like the following, every field which is filled in the example cannot be null:

```
 {
 	"receiptType": "INVOICE",
 	"organizationUnitId": 1,
 	"meta": null,
 	"receiptFileId": null,
 	"senderAddress": {
 		"company": null,
 		"firstName": null,
 		"lastName": null,
 		"street": null,
 		"houseNumber": null,
 		"postalCode": null,
 		"city": null,
 		"countryCode": null
 	},
 	"receiverAddress": {
 		"company": null,
 		"firstName": null,
 		"lastName": null,
 		"street": null,
 		"houseNumber": null,
 		"postalCode": null,
 		"city": null,
 		"countryCode": null
 	},
 	"contactId": null,
 	"positions": [{
 		"title": "test",
 		"description": null,
 		"taxRateId": 1,
 		"quantity": 2,
 		"unit": "PIECES",
 		"singleNetAmount": null,
 		"singleGrossAmount": 119
 	}, {
 		"title": "test",
 		"description": null,
 		"taxRateId": 1,
 		"quantity": 5,
 		"unit": "PIECES",
 		"singleNetAmount": 100,
 		"singleGrossAmount": null
 	}, {
 		"title": "test",
 		"description": null,
 		"taxRateId": 1,
 		"quantity": 5,
 		"unit": "PIECES",
 		"singleNetAmount": 145.23,
 		"singleGrossAmount": null
 	}, {
 		"title": "test",
 		"description": null,
 		"taxRateId": 1,
 		"quantity": 5,
 		"unit": "PIECES",
 		"singleNetAmount": null,
 		"singleGrossAmount": 145.17
 	}],
 	"templateId": "615ac02053166e59daa57f34",
 	"receiptNumber": "RE2021100001",
 	"receiptDate": "2021-10-04",
 	"deliveryDate": "2021-10-04",
 	"benefitPeriodStart": null,
 	"benefitPeriodEnd": null
 }
```

After creating a receipt you can process the receipt to another status (```DRAFT -> OPEN, OPEN -> PAID, OPEN -> CANCELED```). The default status after creating a receipt is always
DRAFT. When the status is changed through processing you cannot edit the receipt anymore (the receipt file was created automatically, while processing).

### Receipt templates

For templating [docsrabbit](https://gitlab.com/encircle360-oss/docsrabbit/docsrabbit) is used. In your receipt creation you should set a docsrabbit template id, or you have defined
default template mappings for your organization.

Read the documentation of docsrabbit for more information: [docsrabbit documentation](https://gitlab.com/encircle360-oss/docsrabbit/docsrabbit/-/blob/master/README.md)