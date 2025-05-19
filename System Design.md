# System Design - Webhook Handler

## Problem Statement

You are tasked with designing a webhook handler for a financial transaction fraud prevention system that notifies customers when the state of their transactions changes. The possible states are APPROVE, REJECT, and REVIEW. These state changes are determined by a separate system and sent to your webhook handler, which is responsible for notifying customers in near real-time.

Additionally, you need to design a configuration interface where customers can set up and manage their webhook settings. This interface should allow customers to:
- Register one or more webhook URLs.
- Specify which transaction states (APPROVE, REVIEW, DECLINE) they want to receive notifications for.
- Test their webhook setup to ensure it is correctly configured.