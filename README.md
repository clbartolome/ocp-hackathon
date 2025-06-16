# ocp-hackathon

Guide [link](https://clbartolome.github.io/ocp-hackathon/ocp-hackathon/index.html)

> [!IMPORTANT]  
> Last tested versions: 
> - OpenShift: 4.18.16
> - OpenShift GitOps: v1.16.1 
> - OpenShift DevSpaces: v3.21.0
> - Web Terminal: 1.13.0

## Hackathon Deployment

### Pre-Requisites

- Install **Web Terminal** operator (default config) in hackathon cluster
- Install **OpenShift GitOps** operator (default config) in hackathon cluster
- Install **OpenShift DevSpaces** operator (default config) in hackathon cluster
- Go to Red Hat OpenShift Dev Spaces Operator -> Red Hat OpenShift Dev Spaces instance Specification -> Create CheCluster with this settings:
    - Development Environments > startTimeoutSeconds: 600
    - Development Environments > storage > pvcStrategy: per-workspace
- Build Ansible Execution Environment Manually:

```sh
cd installation/ansible-navigator
ansible-builder build -t ocp-hackathon-ee:latest
```

### Installation

- Open a terminal

- Login into OpenShift

- Access installation->ansible-navigator: `cd installation/ansible-navigator`

- Create environment vars for configuration (**update Bitwarden token**):

```sh
export OPENSHIFT_TOKEN=$(oc whoami --show-token)
export CLUSTER_DOMAIN=$(oc whoami --show-server | sed 's~https://api\.~~' | sed 's~:.*~~')
```

- Run installation:

```sh
ansible-navigator run ../install.yaml -m stdout \
    -e "ocp_host=$CLUSTER_DOMAIN" \
    -e "api_token=$OPENSHIFT_TOKEN"
```

### Installation

- Open a terminal

- Login into OpenShift

- Access installation->ansible-navigator: `cd installation/ansible-navigator`

- Create environment vars for configuration (**update Bitwarden token**):

```sh
export OPENSHIFT_TOKEN=$(oc whoami --show-token)
export CLUSTER_DOMAIN=$(oc whoami --show-server | sed 's~https://api\.~~' | sed 's~:.*~~')
```

- Run installation:

```sh
ansible-navigator run ../uninstall.yaml -m stdout \
    -e "ocp_host=$CLUSTER_DOMAIN" \
    -e "api_token=$OPENSHIFT_TOKEN"
```

> [!NOTE]  
> Review other created resources by users like devspaces namespaces, other namespaces,...

## Hackathon Break and Fix exercises

A series of exercises have been prepared in which scenarios that cause errors in the application will be generated so that hackathon participants have to fix them. 
The scenarios are based on the resources from the ArgoCD challenge. 

The installation process creates the namespaces admin-app and admin-app-db, which can be used in case the hackathon participants do not complete the ArgoCD exercise.

To run scenarios:

```sh
export OPENSHIFT_TOKEN=$(oc whoami --show-token)
export CLUSTER_DOMAIN=$(oc whoami --show-server | sed 's~https://api\.~~' | sed 's~:.*~~')
 
ansible-navigator run ../scenarios.yaml -m stdout \
    -e "ocp_host=$CLUSTER_DOMAIN" \
    -e "api_token=$OPENSHIFT_TOKEN"
```

Select desired scenarios when asked.

### 1: Postgres DB Update Scenario

This scenario modifies **user-?-argo-db** database password.

Fix: Update application configuration environment variable (DB_PASSWORD)

### 2: Default Quotas Scenario

This scenario modifies **user-?-argo** namespace adding a default limit that brakes demo-app deploy.

Fix: Update application resources configuration (in deployment):
```yaml
resources:
  requests:
    memory: "256Mi"
    cpu: "500m"
  limits:
    memory: "512Mi"
    cpu: "1"
```


### 3: Network Policies Scenario

This scenario modifies **user-?-argo-db** namespace adding a zero-trust policy

Fix: Add a network policy for app -> db comunication:
```yaml
apiVersion: networking.k8s.io/v1
kind: NetworkPolicy
metadata:
  name: allow-from-app-to-postgres
  namespace: user-<id>-argo-db
spec:
  podSelector:
    matchLabels:
      deployment: postgres
  ingress:
    - from:
      - namespaceSelector:
          matchLabels:
            kubernetes.io/metadata.name: user-<id>-argo
  policyTypes:
      - Ingress
```
