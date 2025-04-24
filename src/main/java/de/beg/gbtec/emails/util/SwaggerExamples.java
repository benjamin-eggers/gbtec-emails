package de.beg.gbtec.emails.util;

public class SwaggerExamples {

    private SwaggerExamples() {
    }

    public static final String PROBLEM_EXAMPLE = """
            {
                "title": "Internal Server Error",
                "message": "An unexpected error occurred. Please try again later. If the issue persists please give the following code as reference: 123456",
                "status": "INTERNAL_SERVER_ERROR",
                "location": "/emails"
            }
            """;

    public static final String EMAIL_EXAMPLE =
            """
            {
                "emailId": 1,
                "emailFrom": "sender@example.com",
                "emailTo": [
                    {
                        "email": "recipient@example.com"
                    }
                ],
                "emailCc": [
                    {
                        "email": "cc@example.com"
                    }
                ],
                "emailBcc": [
                    {
                        "email": "bcc@example.com"
                    }
                ],
                "emailSubject": "Lorem ipsum dolor sit amet",
                "emailBody": "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum.",
                "state": "DRAFT"
            }
            """;

    public static final String BULK_CREATE_RESPONSE_EXAMPLE =
           """
           {
            "results": [
                {
                    "status": 201,
                    "id": 1,
                    "data": {
                        "emailId": 1,
                        "emailFrom": "sender@example.com",
                        "emailTo": [
                            {"email": "recipient@example.com"}
                        ],
                        "emailCc": [
                            {"email": "cc@example.com"}
                        ],
                        "emailBcc": [
                            {"email": "bcc@example.com"}
                        ],
                        "emailSubject": "Lorem ipsum dolor sit amet",
                        "emailBody": "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum.",
                        "state": "DRAFT"
                    }
                }
            ]
           }
           """;

}
