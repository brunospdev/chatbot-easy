/*
  Warnings:

  - A unique constraint covering the columns `[telefone]` on the table `Empresa` will be added. If there are existing duplicate values, this will fail.
  - A unique constraint covering the columns `[celular]` on the table `Usuario` will be added. If there are existing duplicate values, this will fail.
  - Made the column `url_api` on table `ConfiguracaoERP` required. This step will fail if there are existing NULL values in that column.
  - Made the column `token_api` on table `ConfiguracaoERP` required. This step will fail if there are existing NULL values in that column.
  - Made the column `cnpj` on table `Empresa` required. This step will fail if there are existing NULL values in that column.
  - Made the column `telefone` on table `Empresa` required. This step will fail if there are existing NULL values in that column.
  - Made the column `nome` on table `Plataforma` required. This step will fail if there are existing NULL values in that column.
  - Made the column `celular` on table `Usuario` required. This step will fail if there are existing NULL values in that column.

*/
-- AlterTable
ALTER TABLE `ConfiguracaoERP` MODIFY `url_api` VARCHAR(191) NOT NULL,
    MODIFY `token_api` VARCHAR(191) NOT NULL;

-- AlterTable
ALTER TABLE `Empresa` MODIFY `cnpj` VARCHAR(191) NOT NULL,
    MODIFY `telefone` VARCHAR(191) NOT NULL;

-- AlterTable
ALTER TABLE `Plataforma` MODIFY `nome` VARCHAR(191) NOT NULL;

-- AlterTable
ALTER TABLE `Usuario` MODIFY `celular` VARCHAR(191) NOT NULL;

-- CreateIndex
CREATE UNIQUE INDEX `Empresa_telefone_key` ON `Empresa`(`telefone`);

-- CreateIndex
CREATE UNIQUE INDEX `Usuario_celular_key` ON `Usuario`(`celular`);
