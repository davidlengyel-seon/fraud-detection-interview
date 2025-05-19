# System Design - Webhook Handler

## Problem Statement

You are tasked with designing a webhook handler for a financial transaction fraud prevention system that notifies customers when the state of their transactions changes. The possible states are APPROVE, REJECT, and REVIEW. These state changes are determined by a separate system and sent to your webhook handler, which is responsible for notifying customers in near real-time.

Additionally, you need to design a configuration interface where customers can set up and manage their webhook settings. This interface should allow customers to:
- Register one or more webhook URLs.
- Specify which transaction states (APPROVE, REJECT, REVIEW) they want to receive notifications for.
- Test their webhook setup to ensure it is correctly configured.
## Requirements:
#### Event Processing:
- The webhook handler will receive events representing state changes for transactions.
- Each event must be processed and the corresponding webhook notification sent to the customer.
- The system should handle high volumes of events, potentially processing thousands of state changes per second.
#### Webhook Delivery:
- The handler must send webhook notifications to customer-provided URLs based on their configuration.
- Ensure that webhook deliveries are reliable, with mechanisms to handle temporary failures, such as network issues or service unavailability on the customer's side.
- Support retry mechanisms with exponential backoff for failed deliveries.
- Track the status of each webhook delivery (e.g., successful, failed, retrying).
#### Scalability and Reliability:
- Design the system to scale horizontally to handle increasing volumes of transaction state changes and customer configurations.
- Ensure the system is highly available and fault-tolerant, minimizing downtime and preventing data loss.
- Consider strategies for ensuring message delivery even in cases of partial system failures.
#### Customer Configuration Interface:
- Create a secure and user-friendly web interface where customers can configure their webhook settings.
- Allow customers to:
	- Register one or more webhook URLs.
	- Choose which transaction states (APPROVE, REJECT, REVIEW) they want notifications for.
    - Test the webhook configuration to validate the setup.
- Provide an API for programmatic configuration and management of webhook settings.
#### Security:
- Implement secure transmission of data, ensuring that webhook payloads are protected in transit.
- Provide a mechanism for customers to authenticate webhook requests to verify that notifications are coming from your system.
- Implement authentication and authorization mechanisms for accessing and configuring webhook settings.
- Handle sensitive transaction data in compliance with relevant financial regulations.
    
#### Monitoring and Observability:
- Implement logging and monitoring to track the performance and health of the webhook handler and the configuration interface.
- Provide metrics on delivery success rates, failure rates, and latency.
- Set up alerts for critical issues, such as a high rate of failed deliveries or configuration errors.
#### Extensibility:
- Design the system to allow easy onboarding of new customers and their webhook URLs.
- Consider how the system could support additional transaction states or notification types in the future.
    
### Follow-up Questions:
    
#### Understanding the Scope:
- "How many state change events should the webhook handler be able to process per second at peak?"
- "How many customers or third-party systems will be subscribing to these webhook notifications?"
- "Are there any specific requirements on how quickly after the state change the webhook should be delivered?"
- "How complex do you envision the customer configuration needs to be? Will most customers have simple or complex rules?"
    
#### Performance and Scalability:
- "What are the latency requirements for processing a state change event and sending a webhook notification?"
- "How would you design the system to handle a significant increase in the number of transactions, customers, and configuration changes over time?"
- "Would you consider using a distributed system or service to ensure scalability? If so, which ones?"
#### Reliability and Fault Tolerance:
- "What strategies would you implement to handle webhook delivery failures, such as network issues or rate limiting on the customer’s side?"
- "How would you ensure that no state change event or customer configuration is lost, even in the case of a system failure?"
- "What retry strategies would you implement for failed webhooks? How would you manage retries to avoid overwhelming the customer’s system?"
#### Customer Configuration Interface:
- "How would you design the user interface for configuring webhooks to ensure it is intuitive and secure?"
- "How would you ensure the configuration changes are reflected immediately in the webhook handling process?"
- "Would you provide a way for customers to test their webhook configurations? If so, how would that be implemented?"
- "How would you design the API for programmatic management of webhook configurations?"
#### Security:
- "How would you secure the transmission of webhook data and ensure that only authorized customers can access and modify their webhook settings?"
- "What mechanisms would you implement for customers to authenticate and verify that webhook requests are legitimate?"
- "How would you ensure compliance with financial regulations when handling transaction data and customer configurations?"
#### Monitoring and Observability:
- "What metrics would you monitor to ensure the webhook handler and configuration interface are performing as expected?"
- "How would you implement logging to track the status and performance of webhook deliveries and configuration changes?"
- "What kind of alerts would you set up to notify you of issues such as high failure rates, configuration errors, or increased latency?"
#### Extensibility:
- "How would you design the system to easily onboard new customers and their webhook URLs?"
- "If additional transaction states or notification types were introduced, how would you ensure the system can adapt to these changes without significant downtime?"
- "How would you structure the system to support different webhook formats or protocols in the future?"
