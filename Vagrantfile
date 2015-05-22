# -*- mode: ruby -*-
# vi: set ft=ruby :

# Vagrantfile API/syntax version. Don't touch unless you know what you're doing!
VAGRANTFILE_API_VERSION = "2"

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|
  # Every Vagrant virtual environment requires a box to build off of.
  config.vm.box = "ubuntu/trusty32"

  # Create a forwarded port mapping which allows access to a specific port
  # within the machine from a port on the host machine.
  config.vm.network :forwarded_port, guest: 2480, host: 2480  # OrientDB
  config.vm.network :forwarded_port, guest: 2424, host: 2424  # OrientDB

  # Create a private network, which allows host-only access to the machine
  # using a specific IP.
  config.vm.network "private_network", ip: "192.168.33.10"
  
  # Use VBoxManage to customize the VM. For example to increase available memory.
  config.vm.provider "virtualbox" do |vb|
    vb.customize ["modifyvm", :id, "--memory", "2048"]
  end

  config.vm.provision "shell", path: "vagrant/bootstrap-once.sh"
  config.vm.provision "shell", path: "vagrant/bootstrap-always.sh", run: "always"

  config.vm.hostname = "ordina.dev"

  # remove tty errors in console
  config.ssh.shell = "bash -c 'BASH_ENV=/etc/profile exec bash'"

end
