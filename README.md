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
- Go to Red Hat OpenShift Dev Spaces Operator -> Red Hat OpenShift Dev Spaces instance Specification -> Create CheCluster (default config - optionally change start timeout)
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
