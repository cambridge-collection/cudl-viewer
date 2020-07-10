Building deploy-data image requires setting the following ENV variables:

export SSH_PUB_KEY=$(cat ~/.ssh/lanc_bot.pub)
export SSH_PRV_KEY=$(cat ~/.ssh/lanc_bot)

Which should be a key with read access to the data-releases repo.
