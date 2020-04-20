_espide_dir = function()
    local k, v, l
    print("~~~File " .. "list START~~~")
    for k, v in pairs(file.list()) do
        l = string.format("%-15s", k)
        print(l .. " : " .. v .. " bytes")
    end
    print("~~~File " .. "list END~~~")
end
_espide_dir()
_espide_dir = nil
