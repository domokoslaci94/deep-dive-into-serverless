# Notes

[Syndicate README](https://github.com/epam/aws-syndicate)

## Generate project

```bash
syndicate generate project --name {task_name}
```

## Generate config

Copy it from the task itself

```bash
syndicate generate config --name "dev" \
    --region "eu-central-1" \
    ...
```

```bash
export SDCT_CONF="{config_location}"
```

## Generate lambda

```bash
syndicate generate lambda \
    --name {lambda_name} \
    --runtime java 
```

## Generate API Gateway

```bash
syndicate generate meta api_gateway \
    --resource_name {api_gateway} \
    --deploy_stage {deploy_stage} \
    --minimum_compression_size 0 
```

### Generate API Gateway path

```bash
syndicate generate meta api_gateway_resource \
    --api_name {api_name} \
    --path {path} \
    --enable_cors false 
```

### Generate API Method for path

```bash
syndicate generate meta api_gateway_resource_method \
    --api_name {api_name} \
    --path {path} \
    --method {method} \
    --integration_type lambda \
    --lambda_name {lambda_name}
```

## Generate sns topic

```bash
syndicate generate meta sns_topic \
    --resource_name {topic_name} \
    --region eu-central-1
```

## Generate DynamoDB Table

```bash
syndicate generate meta dynamodb \
    --resource_name {table_name} \
    --hash_key_name {id} \
    --hash_key_type {type}
```

## Build and deploy

```bash
syndicate create_deploy_target_bucket
```

```bash
syndicate build && syndicate deploy
```

### Update

```bash
syndicate clean && syndicate build && syndicate deploy
```

or

```bash
syndicate clean --rollback && syndicate build && syndicate deploy --replace_output
```