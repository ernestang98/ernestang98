# YouTube Resources on Helm Charts

- [Rahul Wagh Helm Chart Playlist](https://www.youtube.com/watch?v=pHGc87zHLlo&list=PL7iMyoQPMtANm_35XWjkNzDCcsw9vy01b&index=1), written tutorials found [here](https://jhooq.com/series/helm-chart/)

- [Richard Chesterwood Learn Helm with this full "Mini Course"](https://www.youtube.com/playlist?list=PLSwo-wAGP1b8svO5fbAr7ko2Buz6GuH1g)

### Helm Installation

- Provision a k8s cluster (minikube should be fine)

- Install helm depending on your OS, reference [here](https://helm.sh/docs/intro/install/)

### Helm as a package manager

- Navigate to [artifacthub.io](https://artifacthub.io/packages/helm/bitnami/mysql) to view available helm packages (example service to be installed is MySQL)

- Commands:

    ```
    helm repo list
    helm repo add REPOSITORY_NAME REPOSITORY_LINK
    helm repo remove REPOSITORY_NAME
    helm install RELEASE_NAME REPOSITORY_NAME/SERVICE
    helm install RELEASE_NAME /PATH/TO/WHERE/HELM/FILES/ARE
    helm delete/uninstall RELEASE_NAME
    helm list
    ```

    1. RELEASE_NAME cannot contain special characters, spaces, or uppercased letters

- Use case - Setting up monitoring stack:

    ```
    helm repo add prom-repo https://prometheus-community.github.io/helm-charts
    helm repo update
    helm install prom-stack prom-repo/kube-prometheus-stack
    ```

- Customise helm installations

    ```
    kubectl edit svc SERVICE_NAME
    helm upgrade prom-stack prom-repo/kube-prometheus-stack --set grafana.adminPassword=admin
    helm upgrade monitoring prom-repo/kube-prometheus-stack --values=values.yaml (for any required values missing, helm will use default values from repository/directory)
    helm upgrade monitoring prom-repo/kube-prometheus-stack --values=doesnothavetobevalues.yaml
    helm upgrade monitoring prom-repo/kube-prometheus-stack -f doesnothavetobevalues.yaml
    ```

    1. If you already installed a release, you can't reinstall the release using `helm install` but you can use `helm update`

- Extract values from helm charts

    ```
    helm show values prom-repo/kube-prometheus-stack > values.yaml
    ```

- Download helm charts

    ```
    helm pull prom-repo/kube-prometheus-stack
    helm pull prom-repo/kube-prometheus-stack --untar=true
    ```

- Generate your own helm charts from scratch

    ```
    helm create my-own-chart
    ```

    1. `Chart.yaml` - metadata for the chart

    2. `values.yaml` - values to be used in the generation of the yaml files from hem charts

    3. Functions & Piping

    4. Control Flow Logic

    5. Named Templates (`.tpl` file), can either use `include` or `template` in our helm  files

- Publish your created helm charts, reference [here](https://helm.sh/docs/topics/chart_repository/)

    ```
    helm package /path/to/helm/chart/directory
    helm repo index /path/to/helm/chart/package
    ```

- Generate yaml files from helm charts/Validate helm charts

    ```
    helm template /path/to/directory/of/charts
    helm template /path/to/directory/of/charts --set whatever.you.want.to.set=john.doe
    helm template /path/to/directory/of/charts --output-dir=./output
    helm template RELEASE_NAME /path/to/directory/of/charts --values=/path/to/values/file.yaml > compiled-stack.yaml
    
    helm install RELEASE_NAME --dry-run --debug /path/to/directory/of/charts
    ```

    1. `helm template` validates helm charts without connecting to the k8s API while `helm install --dry-run --debug` validates helm charts while connecting it to the k8s API

- Helm Plugins

    ```
    helm plugin list
    helm plugin install URL
    helm plugin uninstall PLUGIN_NAME
    ```

- Other Helm Things:

    ```
    helm lint /path/to/directory/of/charts
    helm rollback RELEASE_NAME VERSION_TO_ROLLBACK_TO
    ```

    1. [Built-In Objects](https://helm.sh/docs/chart_template_guide/builtin_objects/)

    2. Manage multiple helm charts using a [helmfile](https://github.com/roboll/helmfile), a helm chart wrapper. More can be found from [this](https://medium.com/swlh/how-to-declaratively-run-helm-charts-using-helmfile-ac78572e6088) article.

### Additional Reads

- [Snowflake Servers by Marton Fowler](https://martinfowler.com/bliki/SnowflakeServer.html)