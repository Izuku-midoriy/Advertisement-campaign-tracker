# Week 13 — Configuration Management Specification

## Target
Windows development host running Docker Desktop, controlled from an Ansible runner container with the Docker socket mounted.

## Prerequisites and configuration

| Category | Requirement | Configuration or validation |
|---|---|---|
| Packages | Python 3.12 | Ansible runner base image |
| Packages | Ansible Core | Installed with pip in runner image |
| Packages | Docker CLI | Installed with Debian `docker.io`; validated with `docker --version` |
| Packages | `wget`, `grep` | Needed inside the application container for health validation |
| Users and permissions | User able to run Docker Desktop commands | Verify `docker ps` from PowerShell |
| Users and permissions | Runner can access Docker daemon | Mount `/var/run/docker.sock` into runner |
| Folders | Project mounted at `/workspace` | Gives runner access to playbook and evidence paths |
| Files | `ansible/inventory.ini` | Local target inventory |
| Files | `ansible/ansible.cfg` | Inventory and Ansible defaults |
| Files | `ansible/playbook.yml` | Runtime configuration and health validation |
| Files | `ansible/Dockerfile` | Reproducible Ansible runner |
| Network | `advertisement-campaign-tracker_default` | Ensured to exist |
| Services | Docker daemon | Must be reachable |
| Services | `adtracker-mysql` | Existing container must exist; started if stopped |
| Services | `adtracker-app` | Existing container must exist; started if stopped |
| Ports | Host `8081` / container `8081` | Web application and health endpoint |
| Ports | Host `3307` / container `3306` | MySQL access; app connects over Docker network to port 3306 |
| Database | `advertisement_tracker`, user `adtracker` | Existing configuration; credentials are not stored in Ansible files |
| Validation | `/actuator/health` | Must report overall status `UP` |

## Safety
The playbook does not delete database volumes, reset database data, or recreate existing application/database containers. If an expected container is missing, it fails rather than replacing it. Do not put credentials in the playbook or logs.

## Acceptance criteria
- Runner image builds and contains the Docker CLI.
- Playbook completes successfully.
- Existing app and database containers are running and connected to the expected network.
- Application health endpoint reports HTTP 200 and `UP`.
- Second run completes without unnecessary changes.
- Actual first execution is saved to `evidence/ansible-run-1.txt`.
