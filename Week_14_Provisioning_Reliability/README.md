# Week 14 — Automated Provisioning and Reliability Validation

## Objective
Use Ansible and Docker to deploy an isolated application candidate,
validate idempotency, simulate a failed release, and recover the
previous stable application image.

## Environment
- Application: Advertisement Campaign Tracker
- Automation: Ansible
- Runtime: Docker
- Production port: 8081
- Candidate port: 8083
- Database: Existing MySQL container and database

## Implementation
Ansible deployed the candidate using the recorded stable image ID
on the existing Docker network. The existing production application
and database were preserved.

## Validation Results

### 1. Idempotency
The second deployment reported changed=0 and failed=0.
The healthy candidate was retained.

Evidence: evidence/idempotency-run.txt

### 2. Failure Simulation
The candidate was temporarily replaced with Nginx.
The application health endpoint returned HTTP 404 as expected.

Evidence: evidence/failure-simulation.txt

### 3. Rollback and Recovery
Ansible restored the candidate from the previously recorded stable
image. The candidate health endpoint returned HTTP 200 with status UP.

Evidence: evidence/rollback-recovery.txt

### 4. Final Health Verification
The candidate and production health endpoints both returned HTTP 200
with overall status UP.

Evidence: evidence/final-health-check.txt

## Scope Limitation
This exercise provisioned an isolated application container on the
existing Docker host. It did not provision a new virtual machine or
a clean physical node.

## Conclusion
Container-level provisioning, idempotency, health validation, and
rollback/recovery were demonstrated successfully. Production remained
healthy throughout the exercise.
