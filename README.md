## 📝 AWS Feedback System

A scalable feedback collection and sentiment-based analysis system built using **AWS Lambda**, **API Gateway**, **DynamoDB**, **RDS (PostgreSQL)**, 
and a simple HTML dashboard for visualization.

---
## 📌 Project Overview

This project allows users to submit feedback through a POST API, which stores the data in both **Amazon RDS (PostgreSQL)** and **DynamoDB**. 
A separate GET API retrieves the feedback data and displays it in an HTML table. 
It includes sentiment tagging (based on rating), monitoring with **CloudWatch**, and auditing with **CloudTrail**.

---
## ✨ Features

 	•	✅ Submit feedback via POST API
	•	✅ Store feedback in both RDS and DynamoDB
	•	✅ Auto-tag sentiment based on rating (Positive/Negative/Neutral)
	•	✅ Retrieve feedback with GET API
	•	✅ Display feedback in a HTML dashboard
	•	✅ Monitor performance & usage with CloudWatch
	•	✅ Audit events with CloudTrail
	•	✅ Alerts for Lambda errors using CloudWatch Alarms


---
## 🛠️ Tech Stack

| Component         | Technology               |
|------------------|--------------------------|
| Backend (Lambda) | Java 11 (Maven Project)  |
| API Layer        | Amazon API Gateway       |
| Storage          | DynamoDB, PostgreSQL (RDS) |
| Monitoring       | Amazon CloudWatch        |
| Auditing         | AWS CloudTrail           |
| Frontend         | HTML + JavaScript (Fetch API) |


---
## ⚙️ Architecture

       ┌────────────┐
       │  Frontend  │  (HTML Dashboard)
       └─────┬──────┘
             │
        [GET Request]
             │
       ┌─────▼──────┐         ┌────────────┐
       │ API Gateway│ ─────→  │ Lambda GET │ ─────→ DynamoDB
       └─────┬──────┘         └────────────┘

             ▲
       [POST Request]
             │
       ┌─────▼──────┐         ┌────────────┐
       │ API Gateway│ ─────→  │ Lambda POST│ ─────→ DynamoDB + RDS
       └────────────┘         └────────────┘

---
## 📊 CloudWatch Dashboard

Monitors:
	•	Lambda Invocations
	•	Errors & Duration
	•	API Gateway Latency
	•	DynamoDB Read/Write Capacity

Widgets include:
	•	📈 Lambda Invocations (Post & Get)
	•	⚠️ Error Count
	•	⏱️ Duration by Function
	•	🔥 Throttles
	•	🔍 DynamoDB Read/Write Ops
	•	🚨 Alarms Triggered Summary

⸻
## 🔐 CloudTrail Integration

CloudTrail tracks:
	•	API Gateway usage
	•	Lambda executions
	•	DynamoDB and RDS access logs
	•	IAM role activities
 
Useful for auditing API usage and security analysis.

⸻
## 📚 Learning Goals

	•	Understand serverless application architecture on AWS
	•	Work with both SQL and NoSQL data storage in one workflow
	•	Implement basic sentiment logic with conditional logic in Lambda
	•	Use CloudWatch for alerting, dashboards, and insights
	•	Learn to audit and trace AWS usage using CloudTrail

⸻
## 📦 Use Case / Real-World Scenario

This project simulates a real-world scenario where a business wants to:
	•	📥 Collect customer feedback from users about their experience, product, or service.
	•	🧠 Automatically derive sentiment from numerical ratings (positive, neutral, negative).
	•	🧾 Store data in both SQL and NoSQL formats for flexibility in analytics and performance:
	•	DynamoDB for real-time fast retrieval
	•	PostgreSQL for structured reporting and SQL-based querying
	•	📊 Display the feedback to administrators or managers in a web dashboard.
	•	🚨 Automatically alert engineers or DevOps teams in case of system errors or performance issues.
	•	🔍 Maintain a complete audit trail of all actions using CloudTrail for compliance or debugging.

This is especially relevant in industries like:
	•	E-commerce platforms collecting product reviews
	•	SaaS companies analyzing customer satisfaction
	•	Educational platforms gathering student feedback
	•	Mobile apps requesting in-app ratings and comments

⸻
## 📝 Summary

This AWS-powered serverless project demonstrates how to build an end-to-end feedback collection and visualization system, combining:
	•	🔁 Real-time event-driven data flow
	•	📡 RESTful APIs with Lambda integration
	•	🛢️ Dual-database architecture (RDS + DynamoDB)
	•	📊 Monitoring and observability using AWS-native tools
	•	🔒 Secure, auditable, and scalable cloud architecture
 
Whether you’re a cloud engineer, developer, or student, this project offers practical insight into designing robust, scalable, and maintainable applications using modern AWS services.
